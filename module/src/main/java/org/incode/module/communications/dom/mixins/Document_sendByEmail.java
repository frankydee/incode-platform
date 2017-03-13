/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
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
package org.incode.module.communications.dom.mixins;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.isis.applib.ApplicationException;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.background.BackgroundService2;
import org.apache.isis.applib.services.email.EmailService;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.queryresultscache.QueryResultsCache;
import org.apache.isis.applib.services.xactn.TransactionService;

import org.incode.module.communications.dom.impl.commchannel.CommunicationChannel;
import org.incode.module.communications.dom.impl.commchannel.EmailAddress;
import org.incode.module.communications.dom.impl.comms.Communication;
import org.incode.module.communications.dom.impl.comms.CommunicationRepository;
import org.incode.module.communications.dom.spi.CommHeaderForEmail;
import org.incode.module.communications.dom.spi.DocumentCommunicationSupport;
import org.incode.module.document.dom.DocumentModule;
import org.incode.module.document.dom.impl.docs.Document;
import org.incode.module.document.dom.impl.docs.DocumentState;
import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import org.incode.module.document.dom.impl.docs.DocumentTemplateRepository;
import org.incode.module.document.dom.impl.paperclips.PaperclipRepository;
import org.incode.module.document.dom.impl.types.DocumentType;
import org.incode.module.document.dom.services.DocumentCreatorService;

/**
 * Provides the ability to send an email.
 */
@Mixin
public class Document_sendByEmail {

    private final Document document;

    public Document_sendByEmail(final Document document) {
        this.document = document;
    }

    public static class ActionDomainEvent extends DocumentModule.ActionDomainEvent<Document_sendByEmail> { }

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            domainEvent = ActionDomainEvent.class
    )
    @ActionLayout(
            cssClassFa = "at",
            contributed = Contributed.AS_ACTION
    )
    public Communication $$(
            @ParameterLayout(named = "to:")
            final EmailAddress toChannel,
            @Parameter(
                    optionality = Optionality.OPTIONAL,
                    maxLength = CommunicationChannel.EmailType.Meta.MAX_LEN,
                    regexPattern = CommunicationChannel.EmailType.Meta.REGEX,
                    regexPatternReplacement = CommunicationChannel.EmailType.Meta.REGEX_DESC)
            @ParameterLayout(named = "cc:")
            final String cc,
            @Parameter(
                    optionality = Optionality.OPTIONAL,
                    maxLength = CommunicationChannel.EmailType.Meta.MAX_LEN,
                    regexPattern = CommunicationChannel.EmailType.Meta.REGEX,
                    regexPatternReplacement = CommunicationChannel.EmailType.Meta.REGEX_DESC)
            @ParameterLayout(named = "cc (2):")
            final String cc2,
            @Parameter(
                    optionality = Optionality.OPTIONAL,
                    maxLength = CommunicationChannel.EmailType.Meta.MAX_LEN,
                    regexPattern = CommunicationChannel.EmailType.Meta.REGEX,
                    regexPatternReplacement = CommunicationChannel.EmailType.Meta.REGEX_DESC)
            @ParameterLayout(named = "cc (3):")
            final String cc3,
            @Parameter(
                    optionality = Optionality.OPTIONAL,
                    maxLength = CommunicationChannel.EmailType.Meta.MAX_LEN,
                    regexPattern = CommunicationChannel.EmailType.Meta.REGEX,
                    regexPatternReplacement = CommunicationChannel.EmailType.Meta.REGEX_DESC)
            @ParameterLayout(named = "bcc:")
            final String bcc,
            @Parameter(
                    optionality = Optionality.OPTIONAL,
                    maxLength = CommunicationChannel.EmailType.Meta.MAX_LEN,
                    regexPattern = CommunicationChannel.EmailType.Meta.REGEX,
                    regexPatternReplacement = CommunicationChannel.EmailType.Meta.REGEX_DESC)
            @ParameterLayout(named = "bcc (2):")
            final String bcc2
            ) throws IOException {



        if(this.document.getState() == DocumentState.NOT_RENDERED) {
            // this shouldn't happen, but want to fail-fast in case a future programmer calls this directly
            throw new IllegalArgumentException("Document is not yet rendered");
        }

        // create comm and correspondents
        final String atPath = document.getAtPath();
        final String subject = document.getName();
        final Communication communication =  communicationRepository.createEmail(subject, atPath, toChannel, cc, cc2, cc3, bcc, bcc2);

        transactionService.flushTransaction();

        // create and attach cover note
        final DocumentTemplate coverNoteTemplate = determineEmailCoverNoteTemplate();

        final Document coverNoteDoc =
                documentCreatorService.createDocumentAndAttachPaperclips(this.document, coverNoteTemplate);
        coverNoteDoc.render(coverNoteTemplate, this.document);

        paperclipRepository.attach(coverNoteDoc, DocumentConstants.PAPERCLIP_ROLE_COVER, communication);

        // also copy over as attachments to the comm anything else also attached to original document
        final List<Document> communicationAttachments = attachmentProvider.attachmentsFor(document);
        for (Document communicationAttachment : communicationAttachments) {
            paperclipRepository.attach(communicationAttachment, DocumentConstants.PAPERCLIP_ROLE_ATTACHMENT, communication);

        }
        transactionService.flushTransaction();

        // finally, schedule the email to be sent
        communication.scheduleSend();

        return communication;
    }


    public String disable$$() {
        if (emailService == null || !emailService.isConfigured()) {
            return "Email service not configured";
        }
        if (document.getState() != DocumentState.RENDERED) {
            return "Document not yet rendered";
        }
        if(determineEmailCoverNoteTemplate() == null) {
            return "Email cover note type/template not provided";
        }
        if(determineEmailHeader().getDisabledReason() != null) {
            return determineEmailHeader().getDisabledReason();
        }
        if(choices0$$().isEmpty()) {
            return "Could not locate any email address(es) to sent to";
        }
        return null;
    }

    public EmailAddress default0$$() {
        final EmailAddress toDefault = determineEmailHeader().getToDefault();
        if (toDefault != null) {
            return toDefault;
        }
        final Set<EmailAddress> choices = choices0$$();
        return choices.isEmpty() ? null : choices.iterator().next();
    }

    public Set<EmailAddress> choices0$$() {
        return determineEmailHeader().getToChoices();
    }

    public String default1$$() {
        return determineEmailHeader().getCc();
    }

    public String default4$$() {
        return determineEmailHeader().getBcc();
    }


    private DocumentTemplate determineEmailCoverNoteTemplate() {
        DocumentTemplate template = determineEmailCoverNoteTemplateElseNull();
        if(template == null) {
            throw new ApplicationException("Could not locate an email cover note template.");
        }
        return template;
    }

    private DocumentTemplate determineEmailCoverNoteTemplateElseNull() {
        final DocumentType coverNoteDocumentType = determineEmailCoverNoteDocumentType();
        if(coverNoteDocumentType == null) {
            return null;
        }
        return documentTemplateRepository.findFirstByTypeAndApplicableToAtPath(coverNoteDocumentType, document.getAtPath());
    }

    private DocumentType determineEmailCoverNoteDocumentType() {
        return queryResultsCache.execute(() -> {
            if(documentCommunicationSupports != null) {
                for (DocumentCommunicationSupport supportService : documentCommunicationSupports) {
                    final DocumentType documentType = supportService.emailCoverNoteDocumentTypeFor(document);
                    if(documentType != null) {
                        return documentType;
                    }
                }
            }
            return null;
        }, Document_sendByEmail.class, "determineEmailCoverNoteDocumentType", document);
    }

    private CommHeaderForEmail determineEmailHeader() {
        return queryResultsCache.execute(() -> {
            final CommHeaderForEmail commHeaderForEmail = new CommHeaderForEmail();
            if(documentCommunicationSupports != null) {
                for (DocumentCommunicationSupport emailSupport : documentCommunicationSupports) {
                    emailSupport.inferEmailHeaderFor(document, commHeaderForEmail);;
                }
            }
            return commHeaderForEmail;
        }, Document_sendByEmail.class, "determineEmailHeader", document);
    }



    @Inject
    Document_communicationAttachments.Provider attachmentProvider;

    @Inject
    QueryResultsCache queryResultsCache;

    @Inject
    TransactionService transactionService;

    @Inject
    List<DocumentCommunicationSupport> documentCommunicationSupports;

    @Inject
    DocumentTemplateRepository documentTemplateRepository;

    @Inject
    CommunicationRepository communicationRepository;

    @Inject
    PaperclipRepository paperclipRepository;

    @Inject
    EmailService emailService;

    @Inject
    BackgroundService2 backgroundService;

    @Inject
    DocumentCreatorService documentCreatorService;

    @Inject
    FactoryService factoryService;


}
