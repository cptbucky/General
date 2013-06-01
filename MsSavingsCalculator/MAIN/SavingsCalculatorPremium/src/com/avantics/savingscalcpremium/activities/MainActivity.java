package com.avantics.savingscalcpremium.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.avantics.savingscalc.common.IActivity;
import com.avantics.savingscalc.common.Quote;
import com.avantics.savingscalc.common.UiBindingManager;
import com.avantics.savingscalc.common.fragments.QuoteFragment;
import com.avantics.savingscalcpremium.ConfirmDialogFragment;
import com.avantics.savingscalcpremium.ConfirmationDialogHandler;
import com.avantics.savingscalcpremium.DatabaseHelper;
import com.avantics.savingscalcpremium.DeleteQuoteRequestHandler;
import com.avantics.savingscalcpremium.ExcelExporter;
import com.avantics.savingscalcpremium.LoadListItem;
import com.avantics.savingscalcpremium.LoadListItemAdapter;
import com.avantics.savingscalcpremium.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

//import au.com.bytecode.opencsv.CSVWriter;

public class MainActivity extends Activity implements IActivity {

    private static final String HeaderTextKey = "HEADER_TEXT";

    private UiBindingManager binder = null;
    DatabaseHelper dbHelper = null;
    private SparseArray<String> availableQuoteNames;

    private Menu appMenu;
    QuoteFragment quoteFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.premium_quote_form);

        PreferenceManager.setDefaultValues(this, R.layout.settings, false);

        FragmentManager fragManager = getFragmentManager();
        quoteFrag = (QuoteFragment) fragManager.findFragmentById(R.id.quoteFragment);

        binder = quoteFrag.binder;

        getFilesDir().getAbsolutePath();

        dbHelper = new DatabaseHelper(this, 5);

        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString(HeaderTextKey);
            setCurrentTitle(savedText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        appMenu = menu;

        setLoadVisibility();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clearFocusAndKeyboard();

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                saveQuote();

                return true;
            case R.id.action_load:
                loadQuote();

                return true;
            case R.id.action_resetEntry:
                resetQuote();

                return true;
            case R.id.action_send:
                sendQuote();

                return true;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(HeaderTextKey, binder.name);
    }

    private void setLoadVisibility() {
        boolean saveCount = dbHelper.getAvailableQuoteNames().size() > 0;

        MenuItem item = appMenu.findItem(R.id.action_load);
        item.setVisible(saveCount);
    }

    private void clearFocusAndKeyboard() {
        LinearLayout myLayout = (LinearLayout) findViewById(com.avantics.savingscalc.common.R.id.LinearLayout1);
        myLayout.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void resetQuote() {
        binder.resetQuote();
    }

    private void sendQuote() {
        String filePath = String.format("%s/client_date.xls", Environment.getExternalStorageDirectory());

        ExcelExporter xlExporter = new ExcelExporter();

        WritableWorkbook wb = xlExporter.createWorkbook(filePath);

        WritableSheet ws = xlExporter.createSheet(wb, "test sheet", 1);

        Resources resources = getResources();

        try {
            // column headers
            xlExporter.writeCell(1, 3, resources.getString(R.string.statement_total),
                    false, ws);
            xlExporter.writeCell(2, 3, resources.getString(R.string.vendor_rate),
                    false, ws);
            xlExporter.writeCell(3, 3, resources.getString(R.string.calculated_total),
                    false, ws);

            xlExporter.writeCell(0, 4, resources.getString(R.string.cc_short),
                    false, ws);

            xlExporter.writeCell(1, 4, binder.ccst.getValue().toString(), false, ws);
            xlExporter.writeCell(2, 4, binder.ccfr.getValue().toString(), false, ws);
            xlExporter.writeCell(3, 4, binder.cct.getValue().toString(), false, ws);
            xlExporter.writeCell(0, 5, resources.getString(R.string.bc_short),
                    false, ws);
            xlExporter.writeCell(1, 5, binder.bcst.getValue().toString(),
                    false, ws);
            xlExporter.writeCell(2, 5, binder.bcfr.getValue().toString(),
                    false, ws);
            xlExporter.writeCell(3, 5, binder.bct.getValue().toString(),
                    false, ws);
            xlExporter.writeCell(0, 6, resources.getString(R.string.dc_short),
                    false, ws);
            xlExporter.writeCell(1, 6, binder.dcst.getValue().toString(),
                    false, ws);
            xlExporter.writeCell(2, 6, binder.dcfr.getValue().toString(),
                    false, ws);
            xlExporter.writeCell(3, 6, binder.dct.getValue().toString(),
                    false, ws);

            xlExporter.writeCell(2, 7, resources.getString(R.string.vendor_inc_pci),
                    false, ws);
            xlExporter.writeCell(3, 7, binder.fincpci.getValue().toString(),
                    false, ws);

            xlExporter.writeCell(2, 7, resources.getString(R.string.statement_total_exc_terminal),
                    false, ws);
            xlExporter.writeCell(3, 7, binder.stet.getValue().toString(),
                    false, ws);

            xlExporter.writeCell(1, 8, resources.getString(R.string.vendor_terminal),
                    false, ws);
            xlExporter.writeCell(2, 8, binder.vendorterminal.getValue().toString(),
                    false, ws);
            xlExporter.writeCell(3, 8, binder.vendorterminaltotal.getValue().toString(),
                    false, ws);

            xlExporter.writeCell(2, 9, resources.getString(R.string.saving_percentage),
                    false, ws);
            xlExporter.writeCell(3, 9, binder.savingsPercentage.getValue().toString(),
                    false, ws);

            xlExporter.writeCell(2, 10, resources.getString(R.string.savings_month),
                    false, ws);
            xlExporter.writeCell(3, 10, binder.savings1month.getValue().toString(),
                    false, ws);

            xlExporter.writeCell(2, 11, resources.getString(R.string.saving_year),
                    false, ws);
            xlExporter.writeCell(3, 11, binder.savings1year.getValue().toString(),
                    false, ws);

            xlExporter.writeCell(2, 12, resources.getString(R.string.saving_4years),
                    false, ws);
            xlExporter.writeCell(3, 12, binder.savings4years.getValue().toString(),
                    false, ws);
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

        File file = new File(filePath);

        Uri fileUri = Uri.fromFile(file);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"cpt.bucky@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email");
        i.putExtra(Intent.EXTRA_STREAM, fileUri);

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            // Toast.makeText(MyActivity.this,
            // "There are no email clients installed.",
            // Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuote() {
        availableQuoteNames = dbHelper.getAvailableQuoteNames();

        if (availableQuoteNames.size() > 0) {
            final Dialog manageDialog = new Dialog(this);

            manageDialog.setContentView(R.layout.load_dialog);
            manageDialog.setTitle(R.string.load_quote_dialog_title);

            ListView listView1;

            final ArrayList<LoadListItem> load_items = new ArrayList<LoadListItem>();

            for (int i = 0; i < availableQuoteNames.size(); i++) {
                load_items
                        .add(new LoadListItem(availableQuoteNames.valueAt(i)));
            }

            final LoadListItemAdapter adapter = new LoadListItemAdapter(this,
                    R.layout.listview_item_row, load_items);

            adapter.setOnDeleteQuoteRequested(new DeleteQuoteRequestHandler() {

                @Override
                public void callback(LoadListItem arg2) {
                    final String title = arg2.title;

                    ConfirmDialogFragment confirmation = new ConfirmDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("titleString",
                            String.format("Delete Quote %s?", title));
                    args.putString("confirmString", "Delete");
                    confirmation.setArguments(args);
                    confirmation
                            .setOnConfirmed(new ConfirmationDialogHandler() {

                                @Override
                                public void callback() {
                                    Context context = getApplicationContext();
                                    CharSequence text = String.format(
                                            "%s deleted.", title);
                                    int duration = Toast.LENGTH_SHORT;

                                    dbHelper.deleteQuote(title);

                                    if (binder.getSelectedQuote().Name
                                            .equals(title)) {
                                        binder.resetQuote();

                                        setTitle(getResources().getString(
                                                R.string.app_name));
                                    }

                                    availableQuoteNames = dbHelper
                                            .getAvailableQuoteNames();

                                    load_items.clear();

                                    for (int i = 0; i < availableQuoteNames
                                            .size(); i++) {
                                        load_items
                                                .add(new LoadListItem(
                                                        availableQuoteNames
                                                                .valueAt(i)));
                                    }

                                    if (availableQuoteNames.size() == 0) {
                                        manageDialog.dismiss();
                                        setLoadVisibility();
                                    } else {
                                        adapter.notifyDataSetChanged();
                                    }

                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                }
                            });

                    confirmation.show(getFragmentManager(), "confirmDelete");
                }
            });

            listView1 = (ListView) manageDialog.findViewById(R.id.listView1);

            listView1.setAdapter(adapter);

            listView1.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    setSelectedQuote(arg2);

                    manageDialog.dismiss();
                }
            });

            manageDialog.show();
        }
    }

    private void saveQuote() {
        // final EditText input = new EditText(this);

        // LayoutInflater inflater = getLayoutInflater();
        // View dialoglayout = inflater.inflate(R.layout.save_quote_view,
        // (ViewGroup) getCurrentFocus());
        //
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View alertDialogView = factory.inflate(R.layout.save_quote_view,
                null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Save Quote As..")
                .setView(alertDialogView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Do nothing.
                            }
                        });

        final AlertDialog saveDialog = builder.create();

        final ConfirmDialogFragment confirmation = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putString("titleString", "Overwrite Quote?");
        args.putString("confirmString", "Overwrite");
        confirmation.setArguments(args);
        confirmation.setOnConfirmed(new ConfirmationDialogHandler() {

            @Override
            public void callback() {
                EditText saveNameSource = (EditText) alertDialogView
                        .findViewById(R.id.txtQuoteName);

                String quoteName = String.format("%s", saveNameSource.getText()); // currently
                // no
                // validation!!

                Quote quote = binder.getSelectedQuote();
                quote.Name = quoteName;

                dbHelper.updateQuote(quote);

                setCurrentTitle(quote.Name);

                saveDialog.dismiss();

                Context context = getApplicationContext();
                CharSequence text = String.format("%s saved.", quoteName);
                int duration = Toast.LENGTH_SHORT;

                setLoadVisibility();

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        saveDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        saveDialog.show();

        Button btn = saveDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText saveNameSource = (EditText) alertDialogView
                        .findViewById(R.id.txtQuoteName);

                String quoteName = String
                        .format("%s", saveNameSource.getText()); // currently no
                // validation!!

                Boolean wantToCloseDialog = !dbHelper.quoteExists(quoteName);

                if (wantToCloseDialog) {
                    Quote quote = binder.getSelectedQuote();
                    quote.Name = quoteName;

                    dbHelper.addQuote(quote);

                    setCurrentTitle(quote.Name);

                    saveDialog.dismiss();

                    Context context = getApplicationContext();
                    CharSequence text = String.format("%s saved.", quoteName);
                    int duration = Toast.LENGTH_SHORT;

                    setLoadVisibility();

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    confirmation.show(getFragmentManager(), "confirmOverwrite");
                }
            }
        };

        btn.setOnClickListener(listener);
    }

    // private void save(String fileName) {
    // String csv = String.format("%s/%s.csv", getFilesDir(), fileName);
    // CSVWriter writer = null;
    // try {
    // Writer test = new FileWriter(csv, false);
    //
    // writer = new CSVWriter(test);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    //
    // String[] headings = new String[] {
    // getResources().getString(com.avantics.savingscalc.common.R.string.statement_total_exc_terminal),
    // getResources().getString(com.avantics.savingscalc.common.R.string.customer_teminal)
    // };
    //
    // writer.writeNext(headings);
    //
    // String[] values = new String[] {
    // String.valueOf(binder.cstet.getValue()),
    // String.valueOf(binder.csterminal.getValue()) };
    //
    // writer.writeNext(values);
    //
    // try {
    // writer.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // private String[] getFileNames(String root) {
    // File f = new File(root);
    // File[] files = f.listFiles();
    //
    // String[] paths = new String[files.length];
    //
    // for (int i = 0; i < files.length; i++) {
    // // hack assumption that only csvs are going to be stored
    // paths[i] = files[i].getName().replace(".csv", "");
    // }
    //
    // return paths;
    // }

    public void setSelectedQuote(int which) {
        String quoteName = availableQuoteNames.valueAt(which);
        Quote requestedQuote = dbHelper.getQuote(quoteName);

        binder.setSelectedQuote(requestedQuote);

        setCurrentTitle(quoteName);
    }

    private void setCurrentTitle(String title) {
        binder.name = title;

        setTitle(String.format("%s%s",
                getResources().getString(R.string.app_name),
                title.equals("") ? "" : String.format(": %s", title)));
    }
}
