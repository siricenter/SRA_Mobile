package com.example.chad.sraMobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sra.objects.Datapoint;
import com.sra.objects.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class DataGatherQuestionFragment extends Fragment {

    private int questionIndex;
    private Question question;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_data_gather_question,container, false);

        final DataGather activity = (DataGather) getActivity();
        Bundle args = getArguments();
        questionIndex = args.getInt("questionIndex");
        question = activity.getQuestion(questionIndex);

        TextView title = (TextView) rootView.findViewById(R.id.question_fragment_title);
        title.setText(question.getName());

        TableLayout table = (TableLayout) rootView.findViewById(R.id.question_fragment_data_point_table);
        table.setStretchAllColumns(true);

        ArrayList<Datapoint> points = question.getDataPoints();
        for (final Datapoint dp : points) {
            TableRow row = new TableRow(getActivity());

            TextView label = new TextView(getActivity());
            label.setText(dp.getLabel());
            row.addView(label);

            String dataType = dp.getDataType();
            if (dataType.equals("Text")) {
                final EditText input = new EditText(getActivity());
                input.setText(dp.getSingleAnswer());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextAppearance(getActivity(), R.style.TextAppearance_AppCompat_SearchResult_Subtitle);
                input.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {}
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        dp.setSingleAnswer(input.getText().toString());
                    }
                });
                row.addView(input);
            }
            else if (dataType.equals("Number")) {
                final EditText input = new EditText(getActivity());
                input.setText(dp.getSingleAnswer());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setTextAppearance(getActivity(), R.style.TextAppearance_AppCompat_SearchResult_Subtitle);
                input.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {}
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        dp.setSingleAnswer(input.getText().toString());
                    }
                });
                row.addView(input);
            }
            else if (dataType.equals("Date")) {
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePicker datePicker = new DatePicker(getActivity());
                DatePicker.OnDateChangedListener listener = new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(year, monthOfYear, dayOfMonth);
                        dp.setSingleAnswer("" + c.getTimeInMillis());
                    }
                };
                if (!dp.getSingleAnswer().equals("")) {
                    long ms = Long.parseLong(dp.getSingleAnswer());
                    calendar.setTimeInMillis(ms);
                    month = calendar.get(Calendar.MONTH);
                    year = calendar.get(Calendar.YEAR);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                }
                datePicker.init(year, month, day, listener);
                datePicker.setCalendarViewShown(false);

                row.addView(datePicker);
            }
            else if (dataType.equals("Option List")) {
                if (dp.getOptionListType().equals("List")) {
                    final Spinner options = new Spinner(getActivity());
                    ArrayList<String> list = dp.getOptions();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, list);
                    options.setAdapter(adapter);
                    options.setSelection(list.indexOf(dp.getSingleAnswer()));
                    options.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            dp.setSingleAnswer((String) options.getSelectedItem());
                        }
                        @Override public void onNothingSelected(AdapterView<?> parentView) {}
                    });
                    row.addView(options);
                }
                else if (dp.getOptionListType().equals("Checkbox")) {
                    final MultiSelectionSpinner options = new MultiSelectionSpinner(getActivity());
                    options.setItems(dp.getOptions());
                    options.setSelection(dp.getAnswers());
                    options.setOnHoverListener(new View.OnHoverListener() {
                        @Override
                        public boolean onHover(View v, MotionEvent event) {
                            ArrayList<String> arrayListItems = new ArrayList<String>();
                            List<String> items = options.getSelectedStrings();
                            for (String s : items) { arrayListItems.add(s); }
                            dp.setAnswers(arrayListItems);
                            return false;
                        }
                    });
                    row.addView(options);
                }
            }
            table.addView(row);
        }
        return rootView;
    }

    public class MultiSelectionSpinner extends Spinner implements
            OnMultiChoiceClickListener {
        String[] _items = null;
        boolean[] mSelection = null;

        ArrayAdapter<String> simple_adapter;

        public MultiSelectionSpinner(Context context) {
            super(context);

            simple_adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item);
            super.setAdapter(simple_adapter);
        }

        public MultiSelectionSpinner(Context context, AttributeSet attrs) {
            super(context, attrs);

            simple_adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item);
            super.setAdapter(simple_adapter);
        }

        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if (mSelection != null && which < mSelection.length) {
                mSelection[which] = isChecked;

                simple_adapter.clear();
                simple_adapter.add(buildSelectedItemString());
            } else {
                throw new IllegalArgumentException(
                        "Argument 'which' is out of bounds.");
            }
        }

        @Override
        public boolean performClick() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMultiChoiceItems(_items, mSelection, this);
            builder.show();
            return true;
        }

        @Override
        public void setAdapter(SpinnerAdapter adapter) {
            throw new RuntimeException(
                    "setAdapter is not supported by MultiSelectSpinner.");
        }

        public void setItems(String[] items) {
            _items = items;
            mSelection = new boolean[_items.length];
            simple_adapter.clear();
            simple_adapter.add(_items[0]);
            Arrays.fill(mSelection, false);
        }

        public void setItems(List<String> items) {
            _items = items.toArray(new String[items.size()]);
            mSelection = new boolean[_items.length];
            simple_adapter.clear();
            simple_adapter.add(_items[0]);
            Arrays.fill(mSelection, false);
        }

        public void setSelection(String[] selection) {
            for (String cell : selection) {
                for (int j = 0; j < _items.length; ++j) {
                    if (_items[j].equals(cell)) {
                        mSelection[j] = true;
                    }
                }
            }
        }

        public void setSelection(List<String> selection) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            for (String sel : selection) {
                for (int j = 0; j < _items.length; ++j) {
                    if (_items[j].equals(sel)) {
                        mSelection[j] = true;
                    }
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }

        public void setSelection(int index) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }

        public void setSelection(int[] selectedIndicies) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            for (int index : selectedIndicies) {
                if (index >= 0 && index < mSelection.length) {
                    mSelection[index] = true;
                } else {
                    throw new IllegalArgumentException("Index " + index
                            + " is out of bounds.");
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }

        public List<String> getSelectedStrings() {
            List<String> selection = new LinkedList<String>();
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    selection.add(_items[i]);
                }
            }
            return selection;
        }

        public List<Integer> getSelectedIndicies() {
            List<Integer> selection = new LinkedList<Integer>();
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    selection.add(i);
                }
            }
            return selection;
        }

        private String buildSelectedItemString() {
            StringBuilder sb = new StringBuilder();
            boolean foundOne = false;

            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    if (foundOne) {
                        sb.append(", ");
                    }
                    foundOne = true;

                    sb.append(_items[i]);
                }
            }
            return sb.toString();
        }

        public String getSelectedItemsAsString() {
            StringBuilder sb = new StringBuilder();
            boolean foundOne = false;

            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    if (foundOne) {
                        sb.append(", ");
                    }
                    foundOne = true;
                    sb.append(_items[i]);
                }
            }
            return sb.toString();
        }
    }
}