package autohubb.vinture.com.autohubb.Shop;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;


public class ShopListFragment extends Fragment {
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    public String get_mail_from_reg, get_otp_from_reg, get_apikey_from_login, get_email_from_login, get_userid_from_login, get_username_from_login;
    List<List<ShopList>> shoppingItemList;
//    RecyclerView recycler_view;
    ShopListAdapter shopListAdapter;
    public LinearLayout dynamicLayout;
//    TextView cat_name;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg = sharedlogin.getString("get_otp_from_reg", "");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login = sharedlogin.getString("get_email_from_login", "");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login = sharedlogin.getString("get_username_from_login", "");
        shoppingItemList = new ArrayList<>();
//        recycler_view=view.findViewById(R.id.recycler_view);
//        cat_name=view.findViewById(R.id.cat_name);
        dynamicLayout=view.findViewById(R.id.dynamic_layout);




        new ShoppingListItems_Async().execute();
        return view;
    }



//Order Parts API CALL: ---------------------------------------------------------------------->

    public class ShoppingListItems_Async extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            //av_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_SHOPPING_LIST_ITEMS,get_apikey_from_login);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //av_loader.setVisibility(View.GONE);

            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    JSONObject obj=jo.getJSONObject("data");

                    JSONArray dataArray = obj.getJSONArray("shoppingParts");
                    List<ShopList> shoppingPartsSubList = new ArrayList<>();

                    for(int item=0;item<dataArray.length();item++)
                    {
                        JSONObject itemObject=dataArray.getJSONObject(item);
                        String id = itemObject.getString("id");
                        String name = itemObject.getString("name");
                        String description = itemObject.getString("description");
                        String categoryId = itemObject.getString("categoryId");
                        String typeId = itemObject.getString("typeId");
                        String currentStock = itemObject.getString("currentStock");
                        String image = itemObject.getString("image");
                        String price = itemObject.getString("price");
                        String brand= itemObject.getString("brand");
                        String partNumber =itemObject.getString("partNumber");
                        String included =itemObject.getString("included");
                        String productCategory = itemObject.getString("productCategory");
                        String productType = itemObject.getString("productType");

//                            cat_name.setText(categoryName);
                        ShopList shoppingParts = new ShopList(id, name,description,categoryId,typeId,currentStock,image,price,brand,partNumber,included,productCategory,productType);
                        shoppingPartsSubList.add(shoppingParts);
                    }

                    shoppingItemList.add(shoppingPartsSubList);

                    for (List<ShopList> list:shoppingItemList){

                        Log.e("tag","12345-------->");


                        RecyclerView recycler_view = new RecyclerView(getContext());
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
                        recycler_view.setLayoutManager(mLayoutManager);
                        recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
                        recycler_view.setItemAnimator(new DefaultItemAnimator());
                        recycler_view.setNestedScrollingEnabled(false);
                        //TextView textView = new TextView(getContext());

                       // textView.setText(list.get(0).categoryName);
                       // textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.orange_vert, 0, 0, 0);
                       // textView.setTextColor(Color.parseColor("#000000"));
                       // textView.setCompoundDrawablePadding(5);
                       // textView.setTypeface(Typeface.DEFAULT_BOLD);
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        //params.setMargins(10,10,10,10);
                        //textView.setLayoutParams(params);
                       // textView.setTextSize(16);
                       // dynamicLayout.addView(textView);

                        dynamicLayout.addView(recycler_view);
                        shopListAdapter = new ShopListAdapter(getActivity(), list);

                        recycler_view.setAdapter(shopListAdapter);
                    }

                }
                else
                {
                }

            }
            catch (Exception e)
            {

            }



        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}



//IMportatnt

//Before Remove Category***************************


    /*@Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //av_loader.setVisibility(View.GONE);

        try{

            JSONObject jo = new JSONObject(s);
            String status = jo.getString("status");
            if (status.equals("true")) {
                JSONObject obj=jo.getJSONObject("data");

                JSONArray dataArray = obj.getJSONArray("shoppingParts");
                JSONObject dataObject = dataArray.getJSONObject(0);
                JSONArray keys = dataObject.names();
                shoppingItemList = new ArrayList<>();
                for(int i=0;i<keys.length();i++)
                {
                    JSONArray elementArray = dataObject.getJSONArray(keys.getString(i));
                    List<ShopList> shoppingPartsSubList = new ArrayList<>();

                    for (int j = 0; j < elementArray.length(); j++){


                        JSONObject jsonObject1 = elementArray.getJSONObject(j);

                        Log.e("Output","value of i:"+i+" j:"+j+" - "+jsonObject1.toString());

                        String id = jsonObject1.getString("id");
                        String name = jsonObject1.getString("name");
                        String description = jsonObject1.getString("description");
                        String categoryId = jsonObject1.getString("categoryId");
                        String typeId = jsonObject1.getString("typeId");
                        String currentStock = jsonObject1.getString("currentStock");
                        String image = jsonObject1.getString("image");
                        String price = jsonObject1.getString("price");
                        String brand= jsonObject1.getString("brand");
                        String partNumber =jsonObject1.getString("partNumber");
                        String included =jsonObject1.getString("included");
                        String productCategory = jsonObject1.getString("productCategory");
                        String productType = jsonObject1.getString("productType");
                        String categoryName = keys.getString(i);
//                            cat_name.setText(categoryName);
                        ShopList shoppingParts = new ShopList(id, name,description,categoryId,typeId,currentStock,image,price,brand,partNumber,included,categoryName,productCategory,productType);
                        shoppingPartsSubList.add(shoppingParts);
                    }

                    shoppingItemList.add(shoppingPartsSubList);


                }



                for (List<ShopList> list:shoppingItemList){


                    RecyclerView recycler_view = new RecyclerView(getContext());
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
                    recycler_view.setLayoutManager(mLayoutManager);
                    recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    recycler_view.setNestedScrollingEnabled(false);
                    TextView textView = new TextView(getContext());

                    textView.setText(list.get(0).categoryName);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.orange_vert, 0, 0, 0);
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setCompoundDrawablePadding(5);
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,10,10,10);
                    textView.setLayoutParams(params);
                    textView.setTextSize(16);
                    dynamicLayout.addView(textView);

                    dynamicLayout.addView(recycler_view);
                    shopListAdapter = new ShopListAdapter(getActivity(), list);

                    recycler_view.setAdapter(shopListAdapter);
                }


                //recycler_view = new RecyclerView(getContext());






            }
            else
            {
            }

        }
        catch (Exception e)
        {

        }



    }
*/

    //********************************************