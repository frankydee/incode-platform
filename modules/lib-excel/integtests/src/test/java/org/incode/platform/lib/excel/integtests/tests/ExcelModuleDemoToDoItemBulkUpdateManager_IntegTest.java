package org.incode.platform.lib.excel.integtests.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.value.Blob;

import org.isisaddons.module.excel.dom.ExcelService;

import org.incode.platform.lib.excel.integtests.ExcelModuleModuleIntegTestAbstract;

import org.incode.domainapp.example.dom.lib.excel.dom.bulkupdate.ExcelModuleDemoToDoItemBulkUpdateLineItem;
import org.incode.domainapp.example.dom.lib.excel.dom.bulkupdate.ExcelModuleDemoToDoItemBulkUpdateManager;
import org.incode.domainapp.example.dom.lib.excel.dom.bulkupdate.ExcelModuleDemoToDoItemBulkUpdateMenu;
import org.incode.domainapp.example.dom.lib.excel.dom.demo.ExcelModuleDemoToDoItems;
import org.incode.domainapp.example.dom.lib.excel.fixture.RecreateToDoItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExcelModuleDemoToDoItemBulkUpdateManager_IntegTest extends ExcelModuleModuleIntegTestAbstract {

    @Before
    public void setUpData() throws Exception {
        scenarioExecution().install(new RecreateToDoItems());
    }

    @Inject
    private ExcelModuleDemoToDoItems toDoItems;

    @javax.inject.Inject
    private ExcelModuleDemoToDoItemBulkUpdateMenu exportImportService;

    private ExcelModuleDemoToDoItemBulkUpdateManager bulkUpdateManager;

    @Before
    public void setUp() throws Exception {
        bulkUpdateManager = exportImportService.bulkUpdateManager();
    }

    /**
     * Can't do in two steps because the exported XLSX references the ToDoItem's OID which would change if reset db.
     * @throws Exception
     */
    @Test
    public void export_then_import() throws Exception {

        // given
        final byte[] expectedBytes = getBytes(getClass(), "toDoItems-expected.xlsx");

        // when
        final Blob exportedBlob = bulkUpdateManager.export();

        // then
        final byte[] actualBytes = exportedBlob.getBytes();
        // assertThat(actualBytes, lengthWithinPercentage(expectedBytes, 10));  /// ... too flaky


        // and given
        final byte[] updatedBytes = getBytes(getClass(), "toDoItems-updated.xlsx");

        // when
        final List<ExcelModuleDemoToDoItemBulkUpdateLineItem> lineItems =
                bulkUpdateManager.importBlob(new Blob("toDoItems-updated.xlsx", ExcelService.XSLX_MIME_TYPE, updatedBytes));

        // then
        assertThat(lineItems.size(), is(2));

        final ExcelModuleDemoToDoItemBulkUpdateLineItem lineItem1 = lineItems.get(0);
        final ExcelModuleDemoToDoItemBulkUpdateLineItem lineItem2 = lineItems.get(1);

        assertThat(lineItem1.getDescription(), is("Buy milk - updated!"));
        assertThat(lineItem2.getNotes(), is("Get sliced brown if possible."));
    }

    private static byte[] getBytes(final Class<?> contextClass, final String name) throws IOException {
        final ByteSource byteSource = Resources.asByteSource(contextClass.getResource(name));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteSource.copyTo(baos);
        return baos.toByteArray();
    }

    private static final Matcher<? super byte[]> lengthWithinPercentage(final byte[] expectedBytes, final int percentage) {
        return new TypeSafeMatcher<byte[]>() {
            @Override
            protected boolean matchesSafely(byte[] item) {
                final double lower = expectedBytes.length * (100 - percentage) / 100;
                final double upper = expectedBytes.length * (100 + percentage) / 100;
                final int actualLength = item.length;
                return actualLength > lower && actualLength < upper;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Byte array with length within " + percentage + "% of expected length (" + expectedBytes.length + " bytes)");
            }
        };
    }


}