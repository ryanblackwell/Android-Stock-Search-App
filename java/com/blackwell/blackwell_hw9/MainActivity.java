package com.blackwell.blackwell_hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Map;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private AutoCompleteTextView autoComplete;
    public static final String MY_PREFERENCES = "MyPrefs";
    private SharedPreferences sharedPreferences;
    private ArrayList<FavoriteRow> favoriteRowArrayList;
    private FavoritesAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoComplete=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        this.sharedPreferences=getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        this.listView=findViewById(R.id.favorites_list_view);
        this.favoriteRowArrayList=new ArrayList<FavoriteRow>();
        this.adapter=new FavoritesAdapter(this, favoriteRowArrayList);
        this.listView.setAdapter(this.adapter);

        this.updateFavoritesArrayList();
        this.adapter.notifyDataSetChanged();

        registerForContextMenu(this.listView);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteRow faveRow= (FavoriteRow) adapter.getItem(position);
                String ticker=faveRow.getStockTicker();
                Intent intent = new Intent(getApplicationContext(), TabbedStockActivity.class);
                intent.putExtra("stockticker", ticker);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.context_menu_title);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.favorite_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId()==R.id.deleteID)
        {
            String ticker=favoriteRowArrayList.get(info.position).getStockTicker();
            favoriteRowArrayList.remove(info.position);
            adapter.notifyDataSetChanged();
            SharedPreferences.Editor editor=this.sharedPreferences.edit();
            editor.remove(ticker);
            editor.apply();
            Toast.makeText(this, "Selected Yes", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.noDeleteID)
        {
            Toast.makeText(this, "Selected No", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    public void onGetQuoteClick(View V)
    {
        Log.i(TAG, "Get Quote was clicked");
        String curText=autoComplete.getText().toString();
        if(curText==null || curText.trim().length()==0)
        {
            Toast.makeText(getApplicationContext(),
                    "Please enter a stock name or symbol", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, TabbedStockActivity.class);
        intent.putExtra("stockticker", curText);
        startActivity(intent);
    }

    public void onClearClick(View V)
    {
        Log.i(TAG, "Clear was clicked");
        autoComplete.setText("",false);
    }

    private void updateFavoritesArrayList()
    {
        Map<String,?> keys=this.sharedPreferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.i("adding: ",entry.getKey());
            Gson gson = new Gson();
            String json = this.sharedPreferences.getString(entry.getKey(), "");
            FavoriteRow favoriteRowItem=gson.fromJson(json, FavoriteRow.class);
            this.favoriteRowArrayList.add(favoriteRowItem);
        }
    }
}
