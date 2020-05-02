package gofereatsrestarant.views.login;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;

import static gofereatsrestarant.views.login.LoginActivity.alertDialogStores;
import static gofereatsrestarant.views.login.LoginActivity.alertDialogStores2;
import static gofereatsrestarant.views.login.LoginActivity.langclick;


/**
 * Created by trioangle on 31/5/18.
 */

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    public Context context;
    public String language;
    public int lastCheckedPosition = -1;
    public @Inject
    SessionManager sessionManager;
    private List<CurrencyModel> modelItems;


    public LanguageAdapter(Context context, List<CurrencyModel> Items) {
        this.context = context;
        this.modelItems = Items;
        AppController.getAppComponent().inject(LanguageAdapter.this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MovieHolder(inflater.inflate(R.layout.currency_item_view, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_Explore) {
            ((MovieHolder) holder).bindData(modelItems.get(position), position);
        }
        //No else part needed as load holder doesn't bind any data
    }


    @Override
    public int getItemCount() {
        return modelItems.size();
    }




    public class MovieHolder extends RecyclerView.ViewHolder {
        public TextView languagen;
        public TextView languagecode;
        public RadioButton radiobutton;
        public RelativeLayout selectlanguage;

        public MovieHolder(View itemView) {
            super(itemView);
            languagen = (TextView) itemView.findViewById(R.id.currencyname_txt);
            languagecode = (TextView) itemView.findViewById(R.id.currencysymbol_txt);
            radiobutton = (RadioButton) itemView.findViewById(R.id.radioButton1);
            selectlanguage = (RelativeLayout) itemView.findViewById(R.id.selectcurrency);
        }

        public void bindData(final CurrencyModel movieModel, int position) {

            String currencycode;
            currencycode = sessionManager.getLanguage();


            languagen.setText(movieModel.getCurrencyName());
            languagecode.setText(movieModel.getCurrencySymbol());
            languagecode.setVisibility(View.GONE);
            //currency=movieModel.getCurrencyName()+""+movieModel.getCurrencySymbol();

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{

                            context.getResources().getColor(R.color.radio_button_black)
                            , context.getResources().getColor(R.color.appgreen),
                    }
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radiobutton.setButtonTintList(colorStateList);
            }


            radiobutton.setChecked(false);

            if (movieModel.getCurrencyName().equals(currencycode)) {
                radiobutton.setChecked(true);
            }


            selectlanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent x = new Intent(context,Room_details.class);
                    // context.startActivity(x);

                    language = languagen.getText().toString() + " (" + languagecode.getText().toString() + ")";
                    sessionManager.setLanguage(languagen.getText().toString());
                    sessionManager.setLanguageCode(languagecode.getText().toString());


                    lastCheckedPosition = getAdapterPosition();
                    radiobutton.setChecked(true);
                    Locale myLocale = new Locale(movieModel.getCurrencySymbol());
                    Resources res = context.getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                    //new SettingActivity.Updatecurrency().execute();
                    langclick = true;
                    if (alertDialogStores2 != null) {
                        alertDialogStores2.cancel();
                    }
                    if (alertDialogStores != null) {
                        alertDialogStores.cancel();
                    }

                }
            });
        }
    }
}
