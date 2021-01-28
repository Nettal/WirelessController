package n2lf.wirelesscontroller.utilities;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.view.Gravity;
import android.widget.ListView;
import android.view.View;
import android.content.DialogInterface;
import java.util.ArrayList;
import android.text.TextWatcher;
import android.text.Editable;

public class SearchableDialog extends AlertDialog.Builder
{
    AlertDialog alertDialog;
    ListView listView;
    public SearchableDialog(Context context , String[] stringList , int index , IndexChangeListener indexChangeListener){
        super(context);
        ListViewAdapter adapter = new ListViewAdapter(context , stringList , index , indexChangeListener);
        /*
         EditText
         */
        EditText editText = new EditText(context);
        editText.setHint("搜索");
        editText.setAllCaps(false);
        editText.setMaxLines(1);
        editText.addTextChangedListener(adapter);
        editText.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
        editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);//单行输入的前提
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        /*
         ListView
         */
        listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        /*
         LinearLayout
         */
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);//垂直方向
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(editText);
        linearLayout.addView(listView);
        /*
         Dialog
         */
        setView(linearLayout);
        setPositiveButton(Utilities.确定删除[0] , 
            new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface p1, int p2){
                    SearchableDialog.this.alertDialog.dismiss();
                }
            });
        show();
    }


    class ListViewAdapter extends android.widget.BaseAdapter implements TextWatcher{
        Context context;
        IndexChangeListener indexChangeListener;
        TextView[] textViewList;
        ArrayList textViewArrayList;
        TextView selectedTextView;
        int index;
        ListViewAdapter(Context context , String[] stringList , int index){
            this.context = context;
            this.index = index;
            (selectedTextView = new TextView(context)).setText("Warning");
            textViewList = new TextView[stringList.length];
            for(int i = 0; i < stringList.length; i ++){
                textViewList[i] = new TextView(context);
                textViewList[i].setText(stringList[i]);
                textViewList[i].setId(i);
                textViewList[i].setSelected(true);
                textViewList[i].setClickable(true);
                textViewList[i].setTextSize(25);
                textViewList[i].setOnClickListener(new android.view.View.OnClickListener(){
                        @Override
                        public void onClick(View p1){
                            if(((TextView)p1).isSelected()){
                                ((TextView)p1).setSelected(false);
                                ((TextView)p1).setBackgroundColor(android.graphics.Color.alpha(0));
                                selectedTextView.setBackgroundColor(android.graphics.Color.RED);
                            }else{//现在被选中
                                ListViewAdapter.this.setIndex(((TextView)p1).getId());
                                selectedTextView.setBackgroundColor(android.graphics.Color.alpha(0));
                                (selectedTextView = (TextView)p1).setSelected(true);
                                ((TextView)p1).setBackgroundColor(android.graphics.Color.RED);
                            }
                            notifyDataSetChanged();
                        }
                    });
            }
            if(index >= 0 && index < textViewList.length){
                textViewList[index].setSelected(true);
                textViewList[index].setBackgroundColor(android.graphics.Color.RED);
                selectedTextView = textViewList[index];
            }
            textViewArrayList = new ArrayList<TextView>(java.util.Arrays.asList(textViewList));
        }

        ListViewAdapter(Context context , String[] stringList , int index , IndexChangeListener listener){
            this(context , stringList , index);
            indexChangeListener = listener;
        }


        private void setIndex(int i){
            index = i;
            if(indexChangeListener!=null){
                indexChangeListener.onIndexChange(i);
            }
        }

        @Override
        public int getCount(){
            return textViewArrayList.size();
        }

        @Override
        public Object getItem(int p1){
            return textViewArrayList.get(p1);
        }

        @Override
        public long getItemId(int p1){
            return 0;
        }

        @Override
        public View getView(int p1, View p2, android.view.ViewGroup p3){
            return (TextView)textViewArrayList.get(p1);
        }

        /* Text Watcher*/
        @Override
        public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){
        }

        @Override
        public void onTextChanged(CharSequence p1, int p2, int p3, int p4){
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            textViewArrayList = new ArrayList<TextView>(java.util.Arrays.asList(textViewList));
            if(editable != null && editable.length()!=0){//此时输入框不为空
                int maxLength = 0;
                /*去除不合格的item*/
                for(int b = 0; b < textViewArrayList.size();){
                    if(((TextView) textViewArrayList.get(b)).getText().toString().toLowerCase().contains(editable.toString().toLowerCase())){
                        if(((TextView)textViewArrayList.get(b)).getText().length()>=maxLength){
                            maxLength = ((TextView)textViewArrayList.get(b)).getText().length();
                        }
                        b++;
                    }else{
                        //此时的item是不合格的
                        textViewArrayList.remove(b);
                    }
                }
                if(maxLength-editable.length()+1<0){
                    notifyDataSetChanged();
                    return;
                }
                /*关键字排序*/
                ArrayList[] tempList = new ArrayList[maxLength-editable.length()+1];
                for(int i = 0;i<tempList.length;i++){
                    tempList[i]=new ArrayList<TextView>();
                }
                for(int k = 0;k<textViewArrayList.size();k++){//根据index把item塞入templist
                    int index = ((TextView)textViewArrayList.get(k)).getText().toString().toLowerCase().indexOf(editable.toString().toLowerCase());
                    boolean isAdded = false;
                    for(int i = 0;i<tempList[index].size();i++){
                        if(((TextView)textViewArrayList.get(k)).getText().length()<=(((TextView)tempList[index].get(i)).getText().length())){
                            tempList[index].add(i,textViewArrayList.get(k));//有些比列表里的都大
                            isAdded = true;
                            break;
                        }
                    }
                    if(!isAdded){
                        tempList[index].add(textViewArrayList.get(k));
                    }
                }
                textViewArrayList = new ArrayList<TextView>();
                for(int b = 0;b<tempList.length;b++){//塞入排序过的
                    textViewArrayList.addAll(tempList[b]);
                }
            }

            notifyDataSetChanged();
        }
    }


    @Override
    public AlertDialog show(){
        alertDialog = super.show();
        alertDialog.getWindow().setDimAmount(0f);
        return alertDialog;
    }


    public interface IndexChangeListener{
        void onIndexChange(int index);
    }
}
