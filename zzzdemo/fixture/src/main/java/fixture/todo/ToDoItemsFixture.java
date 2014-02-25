/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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

package fixture.todo;

import dom.todo.ToDoItem;
import dom.todo.ToDoItems;

import org.apache.isis.applib.fixtures.AbstractFixture;
import org.apache.isis.objectstore.jdo.applib.service.support.IsisJdoSupport;

public class ToDoItemsFixture extends AbstractFixture {

    private final String user;

    public ToDoItemsFixture() {
        this(null);
    }
    
    public ToDoItemsFixture(String ownedBy) {
        this.user = ownedBy;
    }
    
    @Override
    public void install() {

        final String ownedBy = this.user != null? this.user : getContainer().getUser().getName();
        
        isisJdoSupport.executeUpdate("delete from \"ToDoItem\" where \"ownedBy\" = '" + ownedBy + "'");

        installFor(ownedBy);
        
        getContainer().flush();
    }

    private void installFor(String user) {

        createToDoItemForUser("Review main Isis doc page", "documentation.html", user);
        createToDoItemForUser("Review Isis screenshots", "intro/elevator-pitch/isis-in-pictures.html", user);
        createToDoItemForUser("Lookup some Isis articles", "intro/learning-more/articles-and-presentations.html", user);
        createToDoItemForUser("Figure out dependent drop-downs", "how-tos/how-to-03-022-How-to-specify-dependent-choices-for-action-parameters.html", user);
        createToDoItemForUser("Learn about profiling in Isis", "reference/services/command-context.html", user);

        getContainer().flush();
    }


    // //////////////////////////////////////

    private ToDoItem createToDoItemForUser(final String description, final String documentationPage, String user) {
        return toDoItems.newToDo(description, documentationPage, user);
    }


    // //////////////////////////////////////
    // Injected services
    // //////////////////////////////////////

    @javax.inject.Inject
    private ToDoItems toDoItems;

    @javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;

}
