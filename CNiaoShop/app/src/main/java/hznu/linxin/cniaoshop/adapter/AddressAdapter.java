package hznu.linxin.cniaoshop.adapter;

import android.content.Context;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import hznu.linxin.cniaoshop.R;
import hznu.linxin.cniaoshop.bean.Address;


public class AddressAdapter extends SimpleAdapter<Address> {



    private  AddressLisneter lisneter;

    public AddressAdapter(Context context, List<Address> datas, AddressLisneter lisneter) {
        super(context, R.layout.template_address,datas);

        this.lisneter = lisneter;


    }


    @Override
    protected void convert(BaseViewHolder viewHoder, final Address item) {

        viewHoder.getTextView(R.id.txt_name).setText(item.getConsignee());
        viewHoder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        viewHoder.getTextView(R.id.txt_address).setText(item.getAddr());

        final CheckBox checkBox = viewHoder.getCheckBox(R.id.cb_is_defualt);

        final boolean isDefault = item.getIsDefault();
        checkBox.setChecked(isDefault);


        if(isDefault){
            checkBox.setText("默认地址");
        }
        else{

            checkBox.setClickable(true);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked && lisneter !=null){

                        item.setIsDefault(true);
                        lisneter.setDefault(item);
                    }
                }
            });


        }


    }



    public String replacePhoneNum(String phone){

        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }


   public interface AddressLisneter{


        public void setDefault(Address address);

    }



}
