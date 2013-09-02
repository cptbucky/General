package com.avantics.savingscalcpremium;

import android.content.res.Resources;
import android.util.Log;
import com.avantics.savingscalc.common.Quote;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;

public class ExcelExporter {

    private final WritableSheet ws;
    private final WritableWorkbook wb;
    private WritableCellFormat headerFormat;
    private WritableCellFormat savingsPercentageFormat;
    private WritableCellFormat numberFormat;

    public ExcelExporter(String filePath) {

        WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 12,
                WritableFont.BOLD);
        headerFormat = new WritableCellFormat(headerFont);

        try {
            headerFormat.setAlignment(Alignment.CENTRE);
        } catch (WriteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        WritableFont savingsPercentageFont = new WritableFont(WritableFont.ARIAL, 18, WritableFont.BOLD);
        savingsPercentageFormat = new WritableCellFormat(savingsPercentageFont, NumberFormats.PERCENT_FLOAT);

        try {
            savingsPercentageFormat.setAlignment(Alignment.CENTRE);
            savingsPercentageFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        } catch (WriteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        numberFormat = new WritableCellFormat(NumberFormats.FORMAT8);

        wb = createWorkbook(filePath);

        ws = createSheet(wb, "MSSC Quote", 1);
    }

    public void CreateQuoteWorkSheet(Quote quote, Resources resources) {
        String sheetTitle;

        if (quote.Name.getValue().isEmpty()) {
            sheetTitle = String.format("%s", resources.getString(R.string.app_name));
        } else {
            sheetTitle = String.format("%s: %s", resources.getString(R.string.app_name), quote.Name.getValue());
        }

        try {
            writeHeaderCell(1, 0, sheetTitle);

            /* Incumbent */
            writeHeaderCell(1, 2, resources.getString(R.string.incumbent_cost));

            writeCell(1, 3, resources.getString(R.string.client_statement_total));
            writeValueCell(2, 3, quote.CustomerStatementTotal.getValue());

            writeCell(1, 5, resources.getString(R.string.cc_long));
            writeValueCell(2, 5, quote.CreditCardStatementTotal.getValue());

            writeCell(1, 6, resources.getString(R.string.bc_long));
            writeValueCell(2, 6, quote.BankCardStatementTotal.getValue());

            writeCell(1, 7, resources.getString(R.string.dc_long));
            writeValueCell(2, 7, quote.DebitCardStatementTotal.getValue());
            /* Incumbent */

            /* Proposed */
            writeHeaderCell(4, 2, resources.getString(R.string.proposed_rate));

            writeCell(4, 3, resources.getString(R.string.cc_rate_long));
            writeValueCell(5, 3, quote.CreditCardRate.getValue());

            writeCell(4, 4, resources.getString(R.string.bc_rate_long));
            writeValueCell(5, 4, quote.BankCardRate.getValue());

            writeCell(4, 5, resources.getString(R.string.dc_rate_long));
            writeValueCell(5, 5, quote.DebitCardRate.getValue());

            writeCell(4, 6, resources.getString(R.string.vendor_terminal));
            writeValueCell(5, 6, quote.VendorTerminal.getValue());

            writeCell(4, 7, resources.getString(R.string.vendor_inc_pci));
            writeValueCell(5, 7, quote.FIncludingPciRate.getValue());
            /* Proposed */

            /* Summary */
            writeHeaderCell(7, 2, resources.getString(R.string.summary_section_header));

            writeCell(8, 3, resources.getString(R.string.client_statement_total));
            writeCell(9, 3, resources.getString(R.string.vendor_rate));
            writeCell(10, 3, resources.getString(R.string.vendor_statement_total));

            writeCell(7, 4, resources.getString(R.string.cc_short));
            writeFormulaCell(8, 4, "C5");
            writeFormulaCell(9, 4, "F3");
            writeValueCell(10, 4, quote.CreditCardTotal.getValue());

            writeCell(7, 5, resources.getString(R.string.bc_short));
            writeFormulaCell(8, 5, "C6");
            writeFormulaCell(9, 5, "F4");
            writeValueCell(10, 5, quote.BankCardTotal.getValue());

            writeCell(7, 6, resources.getString(R.string.dc_short));
            writeFormulaCell(8, 6, "C7");
            writeFormulaCell(9, 6, "F5");
            writeValueCell(10, 6, quote.DebitCardTotal.getValue());

            writeCell(8, 7, resources.getString(R.string.vendor_terminal));
            writeFormulaCell(9, 7, "F6");
            writeValueCell(10, 7, quote.VendorTerminalTotal.getValue());

            writeCell(8, 8, resources.getString(R.string.vendor_inc_pci));
            writeFormulaCell(9, 8, "F7");
            writeValueCell(10, 8, quote.FIncludingPciTotal.getValue());

            writeCell(9, 9, "Total");
            writeFormulaCell(10, 9, "SUM(K4:K8)");

            writeHeaderCell(7, 11, resources.getString(R.string.savings_section_header));
            writeCell(10, 11, "");

            writeCell(7, 12, resources.getString(R.string.savings_month));
            writeValueCell(9, 12, quote.SavingsOneMonth.getValue());
            writeValueCell(10, 12, quote.SavingsPercentage.getValue(), savingsPercentageFormat);

            writeCell(7, 13, resources.getString(R.string.saving_year));
            writeValueCell(9, 13, quote.SavingsOneYear.getValue());

            writeCell(7, 14, resources.getString(R.string.saving_4years));
            writeValueCell(9, 14, quote.SavingsFourYears.getValue());

            /* Summary */

            setColumnWidth(1, 20);
            setColumnWidth(2, 10);
            setColumnWidth(4, 20);
            setColumnWidth(7, 5);
            setColumnWidth(8, 15);
            setColumnWidth(9, 15);
            setColumnWidth(10, 15);

            ws.mergeCells(1, 0, 10, 0);

            ws.mergeCells(1, 2, 2, 2);
            ws.mergeCells(4, 2, 5, 2);
            ws.mergeCells(7, 2, 10, 2);

            ws.mergeCells(7, 11, 10, 11);
            ws.mergeCells(7, 12, 8, 12);
            ws.mergeCells(7, 13, 8, 13);
            ws.mergeCells(7, 14, 8, 14);
            ws.mergeCells(10, 12, 10, 14);
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            wb.write();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            wb.close();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void writeCell(int i, int i1, String string) {
        try {
            writeCell(i, i1, string, null);
        } catch (WriteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void writeHeaderCell(int i, int i1, String string) {
        try {
            writeCell(i, i1, string, headerFormat);
        } catch (WriteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void writeValueCell(int i, int i1, double val) {
        try {
            ws.addCell(new Number(i, i1, val, numberFormat));
        } catch (WriteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void writeValueCell(int i, int i1, double val, WritableCellFormat format) {
        try {
            ws.addCell(new Number(i, i1, val, format));
        } catch (WriteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * @param filePath - the name to give the new workbook file
     * @return - a new WritableWorkbook with the given fileName
     */
    public WritableWorkbook createWorkbook(String filePath) {
        // exports must use a temp file while writing to avoid memory hogging
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setUseTemporaryFileDuringWrite(true);

        File wbfile = new File(filePath);

        WritableWorkbook wb = null;

        try {
            // create a new WritableWorkbook using the java.io.File and
            // WorkbookSettings from above
            wb = Workbook.createWorkbook(wbfile, wbSettings);
        } catch (IOException ex) {
            Log.e("test", ex.getStackTrace().toString());
            Log.e("test", ex.getMessage());
        }

        return wb;
    }

    /**
     * @param wb         - WritableWorkbook to create new sheet in
     * @param sheetName  - name to be given to new sheet
     * @param sheetIndex - position in sheet tabs at bottom of workbook
     * @return - a new WritableSheet in given WritableWorkbook
     */
    public WritableSheet createSheet(WritableWorkbook wb, String sheetName,
                                     int sheetIndex) {
        // create a new WritableSheet and return it
        return wb.createSheet(sheetName, sheetIndex);
    }

    /**
     * @param columnPosition - column to place new cell in
     * @param rowPosition    - row to place new cell in
     * @param contents       - string value to place in cell
     * @param CellFormat     - whether to give this cell special formatting
     * @throws RowsExceededException - thrown if adding cell exceeds .xls row limit
     * @throws WriteException        - Idunno, might be thrown
     */
    public void writeCell(int columnPosition, int rowPosition, String contents, WritableCellFormat CellFormat)
            throws RowsExceededException, WriteException {
        // create a new cell with contents at position
        Label newCell = new Label(columnPosition, rowPosition, contents);

        if (CellFormat != null) {
            newCell.setCellFormat(CellFormat);
        }

        ws.addCell(newCell);
    }

    public void writeFormulaCell(int columnPosition, int rowPosition, String formulaString)
            throws RowsExceededException, WriteException {

        Formula formula = new Formula(columnPosition, rowPosition, formulaString);

        formula.setCellFormat(numberFormat);

        ws.addCell(formula);
    }

    public void setColumnWidth(int columnPosition, int columnWidth) {
        ws.setColumnView(columnPosition, columnWidth);
    }
}
