package com.apptech.myndg.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apptech.myndg.R;
import com.apptech.myndg.customviews.DialogController;
import com.apptech.myndg.database.CommonDao;
import com.apptech.myndg.database.PreferenceUtil;
import com.apptech.myndg.model.CarInfo;
import com.apptech.myndg.utils.NdgApp;
import com.apptech.myndg.utils.Values;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nirob on 5/11/18.
 */

public class CarDownloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TITLE_ITEM = 0;
    private static final int CONTENT_ITEM = 1;
    private Context context;
    private Activity activity;
    private List<Object> listData;
    private List<CarInfo> list;
    private CarInfo info = null;
    private CommonDao commonDao;
    private View.OnClickListener itemOnClickListener;
    private int selected;
    private PreferenceUtil preferenceUtil;

    public CarDownloadAdapter(Context context, Activity activity, List<Object> data) {
        this.activity = activity;
        this.context = context;
        this.listData = data;
        commonDao = CommonDao.getInstance();
        // here convert the Object type to CarInfo type data List
        list = (List<CarInfo>) (List<?>) listData;
        preferenceUtil = new PreferenceUtil(context);
    }

    public void setData(List<Object> list) {
        this.listData = list;
        notifyDataSetChanged();
    }

    public void setItemOnClickListener(View.OnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (listData.get(position).getClass() == CarInfo.class) ? TITLE_ITEM : CONTENT_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TITLE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.car_download_row, parent, false);
            view.setOnClickListener(itemOnClickListener);
            return new SimpleViewHolder(view);

        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_download_row_title, parent, false);
            itemView.setOnClickListener(null);
            itemView.setClickable(false);
            return new SectionViewHolder(itemView);
        }
    }

    public void setSelection(int position) {
        selected = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder2, int position) {

        if (listData.get(position).getClass() == CarInfo.class) {
            info = (CarInfo) listData.get(position);

            SimpleViewHolder holder = (SimpleViewHolder) holder2;
            holder.itemView.setTag(position);
            holder.imgDeleteOrDownload.setTag(position);

            if (Values.PREVIOUS_CAR.equalsIgnoreCase(info.getStatus())) {
                NdgApp.getInstance().setPreviousCarImage(info.getId(), holder.imageView);
                holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                if (info.getId() == 1 || info.getId() == 2 || info.getId() == 4 || info.getId() == 5) {
                    String name[] = info.getName().split(" ");
                    holder.txtViewTitle.setText(name[0]);
                } else {
                    holder.txtViewTitle.setText(info.getName());
                }
            } else if (Values.ALREADY_DOWNLOADED.equalsIgnoreCase(info.getStatus())) {
                NdgApp.getInstance().setCarImage(info.getId(), holder.imageView);
                holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.txtViewTitle.setText(info.getName());
            } else {
                NdgApp.getInstance().setCarImage(info.getId(), holder.imageView);
                holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.txtViewTitle.setText(info.getName());
            }

            // here set the download/delete icon in recycler item view
            if (Values.ALREADY_DOWNLOADED.equalsIgnoreCase(info.getStatus())) {
                holder.imgDeleteOrDownload.setBackgroundResource(R.drawable.delete_selector);
            } else {
                holder.imgDeleteOrDownload.setBackgroundResource(R.drawable.download_selector);
            }

            // here set the border visible for just downloaded car
            if (selected == position && Values.ALREADY_DOWNLOADED.equalsIgnoreCase(info.getStatus())) {

                holder.imageViewBorder.setVisibility(View.VISIBLE);
                holder.imgDeleteOrDownload.setVisibility(View.GONE);
                ((CarInfo) listData.get(position)).setSelectedCar(0);

            } else if (info.getSelectedCar() == 1 && Values.ALREADY_DOWNLOADED.equalsIgnoreCase(info.getStatus())) {
                holder.imageViewBorder.setVisibility(View.VISIBLE);
                holder.imgDeleteOrDownload.setVisibility(View.GONE);
                ((CarInfo) listData.get(position)).setSelectedCar(0);
            } else {
                holder.imageViewBorder.setVisibility(View.GONE);
                holder.imgDeleteOrDownload.setVisibility(View.VISIBLE);
            }


        } else {

            SectionViewHolder sectionHolder = (SectionViewHolder) holder2;

            if (context.getResources().getString(R.string.downloaded_car).equalsIgnoreCase(listData.get(position).toString())) {
                sectionHolder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.orange));
                sectionHolder.txtViewSection.setBackgroundResource(R.drawable.downloaded_car);
            } else if (context.getResources().getString(R.string.previous_car).equalsIgnoreCase(listData.get(position).toString())) {
                sectionHolder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                sectionHolder.txtViewSection.setBackgroundResource(R.drawable.previous_model);
            } else {
                sectionHolder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                sectionHolder.txtViewSection.setBackgroundResource(R.drawable.available_for_downlaod);
            }
        }

    }

    public Object getItem(int pos) {
        return listData.get(pos);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private void showCarDownloadDialogForSingleCar(final int position, final boolean isCarDownload) {

        final Dialog dialog = new DialogController(activity).langDialog();

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txt_title);
        if (isCarDownload) {
            txtViewTitle.setText("Do you want to delete?");
        } else {
            txtViewTitle.setText(context.getResources().getString(R.string.car_download_msg));
        }

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (listData.get(position).getClass() == CarInfo.class) {
                    CarInfo info = (CarInfo) listData.get(position);

                    if (info.getId() == 2 || info.getId() == 5) {
                        if (commonDao.getStatus(context, info.getId() - 1) == 2) {
                            commonDao.updateDateAndStatus(context, info.getId(), "2", NdgApp.getInstance().getDateTime(), "RUS");
                            listData.remove(position);
                        } else {
                            commonDao.updateDateAndStatus(context, info.getId(), "2", NdgApp.getInstance().getDateTime(), "EUR");
                            info.setStatus("2");
                        }
                    } else {
                        if (info.getId() == 1 || info.getId() == 4) {
                            commonDao.updateDateAndStatus(context, info.getId(), "2", NdgApp.getInstance().getDateTime(), "EUR");
                            if (commonDao.getStatus(context, info.getId() + 1) == 2) {
                                commonDao.updateDateAndStatus(context, info.getId() + 1, "2", NdgApp.getInstance().getDateTime(), "RUS");
                                listData.remove(position);
                            } else {
                                commonDao.updateDateAndStatus(context, info.getId() + 1, "1", NdgApp.getInstance().getDateTime(), "EUR");
                                info.setStatus("2");
                            }
                        } else {
                            if (info.getId() == 7 || info.getId() == 9) {
                                commonDao.updateDateAndStatus(context, info.getId(), "2", NdgApp.getInstance().getDateTime(), "EUR");
                                info.setStatus("2");
                            } else {
                                commonDao.updateDateAndStatus(context, info.getId(), "0", NdgApp.getInstance().getDateTime(), "EUR");
                                info.setStatus("0");
                            }
                        }
                    }
                    adapterNotify();
                }

            }
        });

        dialog.show();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        ImageView imageViewBorder;
        TextView txtViewTitle;
        ImageButton imgDeleteOrDownload;
        RelativeLayout relativeLayout;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img_view);
            imageViewBorder = (ImageView) view.findViewById(R.id.img_view_border);
            txtViewTitle = (TextView) view.findViewById(R.id.txt_title);
            imgDeleteOrDownload = (ImageButton) view.findViewById(R.id.img_btn_delete_or_download);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_car_download);

            imgDeleteOrDownload.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.img_btn_delete_or_download:

                    int position = (Integer) v.getTag();
                    CarInfo info = (CarInfo) listData.get(position);

                    if (Values.ALREADY_DOWNLOADED.equalsIgnoreCase(info.getStatus())) {
                        showCarDownloadDialogForSingleCar(position, true);
                    }

                    break;

                default:
                    break;
            }

        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView txtViewSection;
        LinearLayout relativeLayout;

        public SectionViewHolder(View itemView) {
            super(itemView);
            txtViewSection = (TextView) itemView.findViewById(R.id.txt_title);
            relativeLayout = (LinearLayout) itemView.findViewById(R.id.relative_section);
        }
    }


    public void adapterNotify() {

        boolean isDownloaded = true;
        boolean isAvailable = true;
        boolean isPrevious = true;

        Collections.sort(list, new Comparator<CarInfo>() {
            @Override
            public int compare(CarInfo o1, CarInfo o2) {
                return o1.getId() - o2.getId();
            }
        });

        Collections.sort(list, new Comparator<CarInfo>() {
            @Override
            public int compare(CarInfo o1, CarInfo o2) {
                if (o1.getId() >= 11) {
                    return o2.getId() - o1.getId();
                } else {
                    return 0;
                }
            }
        });

        final String ORDER = "102";
        Collections.sort(list, new Comparator<CarInfo>() {
            @Override
            public int compare(CarInfo o1, CarInfo o2) {
                return ORDER.indexOf(o1.getStatus()) - ORDER.indexOf(o2.getStatus());
            }
        });

        int index = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus().equalsIgnoreCase("1")) {
                if (isDownloaded) {
                    isDownloaded = false;
                    list.get(i).setSection(true);
                } else {
                    list.get(i).setSection(false);
                }
            } else if (list.get(i).getStatus().equalsIgnoreCase("0")) {
                if (isAvailable) {
                    isAvailable = false;
                    list.get(i).setSection(true);
                } else {
                    list.get(i).setSection(false);
                }

            } else if (list.get(i).getStatus().equalsIgnoreCase("2")) {
                if (isPrevious) {
                    isPrevious = false;
                    list.get(i).setSection(true);
                } else {
                    list.get(i).setSection(false);
                }

            } else {

            }
        }

        CarDownloadAdapter.this.notifyDataSetChanged();
    }

}
