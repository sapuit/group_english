package vn.magik.groupdictionary_as.util;


import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;

import retrofit.http.Path;
import vn.magik.groupdictionary_as.entities.Group;
import vn.magik.groupdictionary_as.entities.ResponseConfig;
import vn.magik.groupdictionary_as.entities.ResponseGroup;
import vn.magik.groupdictionary_as.entities.ResponseVoca;

/**
 * Created by phuoc on 12/30/2015.
 */
public interface ApiInterface {

    @GET("group")
    Call<ResponseGroup> getListGroup();

    @GET("getconfig")
    Call<ResponseConfig> getConfig();

    @GET("group/{pos}")
    Call<ResponseVoca> getListVoca(@Path("pos") String pos);
}
