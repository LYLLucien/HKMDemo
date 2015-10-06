package com.lucien.hkmdemo.constant;

/**
 * Created by lucien.li on 2015/10/5.
 */
public interface Enumeration {

    enum ApiStatus {
        Success,
        General_Error,
        JSON_Error,
        IO_Error,
        Connection_Error, ApiStatus;

        public static ApiStatus get(int position) {
            return ApiStatus.values()[position];
        }

        public static String getTitle(int position) {
            return ApiStatus.get(position).name();
        }
    }
}
