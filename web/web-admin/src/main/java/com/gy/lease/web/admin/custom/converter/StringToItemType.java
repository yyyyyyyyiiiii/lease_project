package com.gy.lease.web.admin.custom.converter;


import com.gy.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToItemType implements Converter<String, ItemType> {

    @Override
    public ItemType convert(String source) {
        Integer s = Integer.parseInt(source);

        for(ItemType itemType : ItemType.values())
        {
            if( itemType.getCode().equals(s)){
                return itemType;
            }
        }
        return null;
    }
}
