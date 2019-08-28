package autohubb.vinture.com.autohubb.splash;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import autohubb.vinture.com.autohubb.R;

class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private int imagesList[];
    private int screenWidth;
    private int screenHeight;
    private int textList[];
    public ViewPagerAdapter(Context mContext, int images[],int list[], int screenWidth,
                            int screenHeight) {
        this.imagesList = images;
        this.mContext = mContext;
        this.screenWidth = screenWidth / 2;
        this.screenHeight = screenHeight / 2;
        this.textList=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imagesList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Declare Variables
        ImageView imgflag;
        TextView tv;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.splash_row_image_gallery, container,
                false);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        // Locate the ImageView in row_image_gallery
        imgflag = (ImageView) itemView.findViewById(R.id.hotspotImage);
        // Capture position and set to the ImageView
        //tv = (TextView) itemView.findViewById(R.id.tv);
        imgflag.setImageResource(imagesList[position]);

        //tv.setText(""+textList[position]);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgflag
                .getLayoutParams();

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        imgflag.setLayoutParams(layoutParams);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
