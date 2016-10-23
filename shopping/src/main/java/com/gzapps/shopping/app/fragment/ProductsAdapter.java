/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gzapps.shopping.R;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.gzapps.shopping.core.Time.DAY;
import static com.gzapps.shopping.core.Time.WEEK;

class ProductsAdapter extends BaseAdapter implements SectionIndexer {

    static final int TYPE_PRODUCT = 1;
    static final int TYPE_DIVIDER = 0;
    final Shopping shopping;
    final Fragment fragment;
    private final Animation animation;
    List<Item> items;
    SparseIntArray sectionForPosition;
    SparseIntArray positionForSection;
    String[] sections;
    int search;
    int highlight = -1;

    ProductsAdapter(Shopping shopping, ProductsFragment fragment) {
        this.shopping = shopping;
        this.fragment = fragment;

        FragmentActivity activity = fragment.getActivity();
        animation = AnimationUtils.loadAnimation(activity, R.anim.highlight);
    }

    public void init() {
        load();
    }

    private void load() {
        List<Product> products = list();

        if (separate()) {
            items = Item.separate(products);
        } else {
            items = Item.itemize(products);
        }

        List<String> divisions = new ArrayList<String>(products.size());
        sectionForPosition = new SparseIntArray();
        positionForSection = new SparseIntArray();

        int section = 0;
        int position = 0;

        for (Item item : items) {
            sectionForPosition.append(position, section);
            positionForSection.append(section, position);

            if (item.divider != null) {
                Divider divider = item.divider;
                divisions.add(divider.name);
                section += 1;
            }

            position += 1;
        }

        sections = divisions.toArray(new String[divisions.size()]);
    }

    public void refresh() {
        load();
        notifyDataSetChanged();
    }

    List<Product> list() {
        return shopping.products();
    }

    boolean separate() {
        return true;
    }

    boolean tick() {
        return true;
    }

    public int search(String text) {
        if (search >= items.size()) {
            search = 0;
        }

        String lowerCaseText = text.toLowerCase(Locale.getDefault());

        for (int i = search; i < items.size(); ++i) {
            Item item = items.get(i);
            if (item.product != null) {
                Product product = item.product;
                String name = product.name();
                String lowerCaseName = name.toLowerCase(Locale.getDefault());
                if (lowerCaseName.contains(lowerCaseText)) {
                    search = i + 1;
                    return i;
                }
            }
        }

        for (int i = 0; i < search; ++i) {
            Item item = items.get(i);
            if (item.product != null) {
                Product product = item.product;
                String name = product.name();
                String lowerCaseName = name.toLowerCase(Locale.getDefault());
                if (lowerCaseName.contains(lowerCaseText)) {
                    search = i + 1;
                    return i;
                }
            }
        }
        return -1;
    }

    public int search(Product product) {
        for (int i = 0; i < items.size(); ++i) {
            Item item = items.get(i);
            if (product.equals(item.product)) {
                return i;
            }
        }

        return -1;
    }

    public void highlight(int position) {
        highlight = position;
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Item item = items.get(position);
        if (item.divider != null) {
            return TYPE_DIVIDER;
        }
        return TYPE_PRODUCT;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        Item item = items.get(position);
        if (item.divider != null) {
            Divider divider = item.divider;
            return divider.id;
        }
        Product product = item.product;
        return product.id();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        Item item = items.get(position);
        return item.product != null;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Item item = items.get(position);

        if (item.product != null) {
            view = getProductView(item, view, parent);
        } else {
            view = getDividerView(item, view, parent);
        }

        if (highlight == position) {
            view.setAnimation(animation);
            highlight = -1;
        } else {
            view.setAnimation(null);
        }

        return view;
    }

    View getDividerView(Item item, View view, ViewGroup parent) {
        Divider divider = item.divider;
        ItemHolder holder;

        if (view == null) {
            LayoutInflater layoutInflater = fragment.getLayoutInflater(null);
            view = layoutInflater.inflate(R.layout.item_divider, parent,
                    false);

            holder = new ItemHolder();
            view.setTag(holder);

            holder.divider =
                    (TextView) view.findViewById(R.id.products_item_divider);
        } else {
            holder = (ItemHolder) view.getTag();
        }

        holder.divider.setText(divider.name);

        return view;
    }

    View getProductView(Item item, View view, ViewGroup parent) {
        Product product = item.product;
        ItemHolder holder;

        if (view == null) {
            LayoutInflater layoutInflater = fragment.getLayoutInflater(null);
            view = layoutInflater
                    .inflate(R.layout.item_products, parent, false);

            holder = new ItemHolder();
            view.setTag(holder);

            holder.name = (CheckedTextView) view
                    .findViewById(R.id.products_item_name);
            holder.time =
                    (TextView) view.findViewById(R.id.products_item_time_left);
            holder.period =
                    (TextView) view.findViewById(R.id.products_item_period);
            holder.quantity =
                    (ProgressBar) view.findViewById(R.id
                            .product_item_quantity);
        } else {
            holder = (ItemHolder) view.getTag();
        }

        Checkable checkable = (Checkable) view;
        checkable.setChecked(item.checked);

        holder.name.setText(product.name());
        if (tick()) {
            holder.name.setChecked(product.enlisted());
        } else {
            holder.name.setCheckMarkDrawable(R.drawable.nothing);
        }

        long distance = product.distance();
        if (distance >= 0) {
            long time = System.currentTimeMillis();
            long timeLeft = product.timeLeft(time);

            String timeLeftText = periodToString(timeLeft);
            holder.time.setText(timeLeftText);
            holder.time.setVisibility(View.VISIBLE);

            String periodText = periodToString(distance);
            holder.period.setText(periodText);
            holder.period.setVisibility(View.VISIBLE);

            float supply = product.supply(time);
            int progress = (int) (supply * 100);
            holder.quantity.setProgress(progress);
            holder.quantity.setVisibility(View.VISIBLE);
        } else {
            holder.time.setText(null);
            holder.time.setVisibility(View.INVISIBLE);

            holder.period.setText(null);
            holder.period.setVisibility(View.INVISIBLE);

            holder.quantity.setProgress(0);
            holder.quantity.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    String periodToString(long time) {
        Resources resources = fragment.getResources();

        int weeks = (int) (time / WEEK);
        int days = (int) (time % WEEK / DAY);

        String weeksText = "";
        if (weeks > 0) {
            weeksText =
                    resources.getQuantityString(R.plurals.weeks, weeks, weeks);
        }

        String daysText = "";
        if (days > 0) {
            daysText = resources.getQuantityString(R.plurals.days, days, days);
        }

        return String.format("%s %s", weeksText, daysText).trim();
    }

    public void toggle(int position) {
        Item item = getItem(position);
        item.checked = !item.checked;
        notifyDataSetChanged();
    }

    public int checkedCount() {
        int count = 0;
        for (Item item : items) {
            if (item.checked) {
                count += 1;
            }
        }
        return count;
    }

    public List<Product> checked() {
        List<Product> checked = new ArrayList<Product>();
        for (Item item : items) {
            if (item.checked) {
                Product product = item.product;
                checked.add(product);
            }
        }
        return checked;
    }

    public void clearChecked() {
        for (Item item : items) {
            item.checked = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public String[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {
        return positionForSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionForPosition.get(position);
    }

    static class ItemHolder {

        TextView divider;
        CheckedTextView name;
        TextView time;
        TextView period;
        ProgressBar quantity;
    }
}
