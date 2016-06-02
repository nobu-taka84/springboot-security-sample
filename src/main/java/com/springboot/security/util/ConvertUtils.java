package com.springboot.security.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.springboot.security.exception.SystemException;

public class ConvertUtils {

    /**
     * convert List&lt;T&gt; to List&lt;U&gt;
     *
     * @param source
     * @param target
     * @return List&lt;U&gt;
     */
    public static <T, U> List<U> copyProperties(List<T> source, Class<U> target) {
        List<U> out = new ArrayList<U>();
        try {
            for (T t : source) {
                U u = target.newInstance();
                BeanUtils.copyProperties(t, u);
                out.add(u);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SystemException("Error", "copyProperties(List<T>, Class<U>)でエラー");
        }

        return out;
    }

    /**
     * convert Page&lt;T&gt; to Page&lt;U&gt;
     * 
     * @param source
     * @param target
     * @return List&lt;U&gt;
     */
    public static <T, U> Page<U> copyProperties(Page<T> source, Class<U> target) {
        List<U> out = copyProperties(source.getContent(), target);
        Pageable pageable = new PageRequest(source.getNumber(), source.getSize());
        return new PageImpl<>(out, pageable, source.getTotalElements());
    }
}
