/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.command.dom;

import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.isis.applib.annotation.Command.ExecuteIn;
import org.apache.isis.applib.annotation.Command.Persistence;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.services.command.Command;
import org.apache.isis.applib.services.command.Command.Executor;
import org.apache.isis.applib.services.command.spi.CommandService;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.objectstore.jdo.applib.service.JdoColumnLength;

/**
 *
 */
@DomainService(
        nature = NatureOfService.DOMAIN
)
public class CommandServiceJdo implements CommandService {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(CommandServiceJdo.class);

    //region > create (API)
    /**
     * Creates an {@link CommandJdo}, initializing its 
     * {@link Command#setExecuteIn(org.apache.isis.applib.annotation.Command.ExecuteIn)} nature} to be
     * {@link org.apache.isis.applib.services.command.Command.Executor#OTHER rendering}.
     */
    @Programmatic
    @Override
    public Command create() {
        CommandJdo command = factoryService.instantiate(CommandJdo.class);
        command.setExecutor(Executor.OTHER);
        command.setPersistence(Persistence.IF_HINTED);
        return command;
    }
    //endregion

    //region > startTransaction (API)

    @Programmatic
    @Override
    public void startTransaction(final Command command, final UUID transactionId) {
        if(command instanceof CommandJdo) {
            // should be the case, since this service created the object in the #create() method
            final CommandJdo commandJdo = (CommandJdo) command;
            final UUID currentTransactionId = commandJdo.getTransactionId();
            if(currentTransactionId != null && !currentTransactionId.equals(transactionId)) {
                // the logic in IsisTransaction means that any subsequent transactions within a given command
                // should reuse the xactnId of the first transaction created within that interaction.
                throw new IllegalStateException("Attempting to set a different transactionId on command");
            }
            commandJdo.setTransactionId(transactionId);
        }
    }
    //endregion

    //region > complete (API)

    @Programmatic
    @Override
    public void complete(final Command command) {
        final CommandJdo commandJdo = asUserInitiatedCommandJdo(command);
        if(commandJdo == null) {
            return;
        }
        if(commandJdo.getCompletedAt() != null) {
            // already attempted to complete.
            // chances are, we're here as the result of a redirect following a previous exception
            // so just ignore.
            return;
        }
            
        // can't store target if too long (eg view models)
        if (commandJdo.getTargetStr() != null && commandJdo.getTargetStr().length() > JdoColumnLength.BOOKMARK) {
            commandJdo.setTargetStr(null);
        }
        commandJdo.setCompletedAt(Clock.getTimeAsJavaSqlTimestamp());

        repositoryService.persist(commandJdo);
    }

    //endregion

    //region > persistIfPossible (API)

    @Override
    public boolean persistIfPossible(Command command) {
        if(!(command instanceof CommandJdo)) {
            // ought not to be the case, since this service created the object in the #create() method
            return false;
        }
        final CommandJdo commandJdo = (CommandJdo)command;
        repositoryService.persist(commandJdo);
        return true;
    }
    //endregion

    //region > helpers


    /**
     * Not API, also used by {@link CommandServiceJdoRepository}.
     */
    CommandJdo asUserInitiatedCommandJdo(final Command command) {
        if(!(command instanceof CommandJdo)) {
            // ought not to be the case, since this service created the object in the #create() method
            return null;
        }
        if(command.getExecuteIn() != ExecuteIn.FOREGROUND) {
            return null;
        } 
        final CommandJdo commandJdo = (CommandJdo) command;
        return commandJdo.shouldPersist()? commandJdo: null;
    }

    //endregion


    @Inject
    RepositoryService repositoryService;
    @Inject
    FactoryService factoryService;

}
