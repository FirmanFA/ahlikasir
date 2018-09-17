package com.ahlikasir.aplikasi.kasironline.Client;

import java.util.List;

import com.ahlikasir.aplikasi.kasironline.model.login.Login;
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang;
import com.ahlikasir.aplikasi.kasironline.model.kelompok.Kelompok;
import com.ahlikasir.aplikasi.kasironline.model.login.Register;
import com.ahlikasir.aplikasi.kasironline.model.pelanggan.Pelanggan;
import com.ahlikasir.aplikasi.kasironline.model.satuan.Satuan;
import com.ahlikasir.aplikasi.kasironline.model.toko.Toko;
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoType;
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoUpIn;
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Pembayaran;
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan;
import com.ahlikasir.aplikasi.kasironline.model.transaksi.PenjualanPelanggan;
import com.ahlikasir.aplikasi.kasironline.model.transaksi.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface BloonClient {

    String key = "X-API-KEY";

    @GET("kelompok/{emailKelompok}")
    Call<List<Kelompok>> getKelompok(@Path("emailKelompok")String email, @Query(key)String token);

    @POST("kelompok")
    Call<ResponseBody> insertKelompok(@Body Kelompok kelompok, @Header(key) String token);

    @DELETE("kelompok/{id}")
    Call<Kelompok> deleteKelompok(@Path("id")String id,@Header(key) String token);

    @GET("kelompok/{email}/{id}")
    Call<Kelompok> getKelompokById(@Path("email") String email,@Path("id")String id,@Query(key) String token);

    @PUT("kelompok/{id}")
    Call<Kelompok> updateKelompok(@Path("id")String id,@Body Kelompok kelompok,@Header(key) String token);

    @GET("satuan/{email}")
    Call<List<Satuan>> getSatuan(@Path("email")String email,@Query(key)String token);

    @GET("satuan/{email}/{id}")
    Call<Satuan> getSatuanById(@Path("email")String email,@Path("id")String id ,@Query(key)String token);

    @POST("satuan")
    Call<ResponseBody> insertSatuan(@Body Satuan satuan,@Header(key)String token);

    @DELETE("satuan/{id}")
    Call<Satuan> deleteSatuan(@Path("id")String id,@Header(key) String token);

    @PUT("satuan/{id}")
    Call<Satuan> updateSatuan(@Path("id")String id,@Body Satuan satuan,@Header(key) String token);

    @GET("pelanggan/{email}")
    Call<List<Pelanggan>> getPelanggan(@Path("email")String email,@Query(key)String token);

    @DELETE("pelanggan/{id}")
    Call<Pelanggan> deletePelanggan(@Path("id")String id,@Header(key) String token);

    @GET("pelanggan/{email}/{id}")
    Call<Pelanggan> getPelangganById(@Path("email")String email,@Path("id")String id ,@Query(key)String token);

    @POST("pelanggan")
    Call<ResponseBody> insertPelanggan(@Body Pelanggan pelanggan,@Header(key)String token);

    @PUT("pelanggan/{id}")
    Call<Pelanggan> updatePelanggan(@Path("id")String id,@Body Pelanggan pelanggan,@Header(key) String token);

    @GET("toko/{email}/{id}")
    Call<Toko> getTokoById(@Path("email") String email,@Path("id") String id, @Query(key)String token);

    @GET("toko/{email}")
    Call<TokoUpIn> getTokoByEmail(@Path("email") String email, @Query(key)String token);

    @POST("toko/0")
    Call<TokoType> cekEmail(@Body Toko toko, @Header(key)String token);

    @PUT("toko")
    Call<ResponseBody> updateToko(@Body TokoUpIn toko,@Header(key) String token);

    @POST("toko/1")
    Call<ResponseBody> insertToko(@Body TokoUpIn toko, @Header(key)String token);

    @GET("barang/{email}")
    Call<List<Barang>> getBarang(@Path("email") String email, @Query(key)String token);

    @GET("kelompok/id/{kelompok}/{email}")
    Call<Kelompok> getIdKelompok(@Path("kelompok")String kelompok,@Path("email")String email,@Query(key)String token);

    @GET("barang/kel/{kelompok}/{email}")
    Call<List<Barang>> getBarangByKelompok(@Path("kelompok")String idkelompok,@Path("email")String email,@Query(key)String token);

    @GET("barang/{email}/{id}")
    Call<Barang> getBarangById(@Path("email")String email,@Path("id")String idbarang,@Query(key)String token);

    @DELETE("barang/{id}")
    Call<Barang> deleteBarang(@Path("id") String id, @Header(key)String token);

    @POST("barang")
    Call<ResponseBody> insertBarang(@Body Barang barang,@Header(key)String token);

    @PUT("barang/{id}")
    Call<Barang> updateBarang(@Path("id")String id,@Body Barang barang,@Header(key)String token);

    @POST("login")
    Call<Login> login(@Body Login login);

    @POST("register")
    Call<Login> register(@Body Register register);

    @POST("login/lupa")
    Call<Login> resetPassword(@Body Login login);

    @GET("penjualan/getfaktur/{email}")
    Call<Login> getFaktur(@Path("email") String email,@Query(key)String token);

    @POST("penjualan")
    Call<ResponseBody> penjualan(@Body Penjualan penjualan, @Header(key)String token);

    @GET("barang/getsatuan/{idbarang}")
    Call<Satuan> getSatuanByBarang(@Path("idbarang")String idbarang,@Query(key)String token);

    @GET("penjualan/detailjual/{email}/{faktur}")
    Call<List<Penjualan>> getPejualanByFaktur(@Path("email")String email,@Path("faktur")String faktur,@Query(key)String token);

    @DELETE("penjualan/detailjual/{id}/{indexsatuan}")
    Call<ResponseBody> deleteDetailJual(@Path("id")String id,@Path("indexsatuan")String indexsatuan,@Header(key)String token);

    @PUT("pembayaran")
    Call<ResponseBody> bayar(@Body Pembayaran pembayaran, @Header(key)String token);

    @GET("pembayaran/{email}")
    Call<List<Pembayaran>> getPembayaran(@Path("email")String email, @Query(key)String token);

    @GET("user/{email}")
    Call<List<User>> getUser(@Path("email")String email,@Query(key)String token);

    @DELETE("user/{id}")
    Call<User> deleteUser(@Path("id")String id,@Header(key)String token);

    @GET("user/{email}/{id]")
    Call<User> getUserById(@Path("email")String email, @Path("id")String id,@Query(key)String token);

    @POST("user")
    Call<User> insertUser(@Body User user,@Header(key)String token);

    @PUT("user/{id}")
    Call<User> updateUser(@Path("id")String id, @Body User user, @Header(key)String token);

    @GET("user/cekuser/{emailuser}")
    Call<User> cekUser(@Path("emailuser")String emailuser,@Query(key)String token);

    @GET("toko/maxdata/{type}/{email}")
    Call<TokoUpIn> cekMaxData(@Path("type")String type,@Path("email")String email,@Query(key)String token);

    @PUT("penjualan")
    Call<PenjualanPelanggan> updateIdPelanggan(@Body PenjualanPelanggan penjualanPelanggan,@Header(key)String token);

    @GET("pendapatan/{email}/{tglawal}/{tglakhir}")
    Call<Penjualan> getPendapatan(@Path("email")String email,@Path("tglawal")String tglawal,@Path("tglakhir")String tglakhir,
                                    @Query(key)String token);

    @GET("penjualan/jualbytgl/{email}/{tanggalawal}/{tglakhir}")
    Call<List<Penjualan>> getPenjualan(@Path("email")String email,@Path("tanggalawal")String tglawal,@Path("tglakhir")String tglakhir,
                                        @Query(key)String token);

    @GET("pendapatan/laporan/{tglawal}/{tglakhir}/{emailuser}")
    Call<List<Penjualan>> lapPendapatan(@Path("tglawal")String tglawal,@Path("tglakhir")String tglakhir,
                                        @Path("emailuser")String email,@Query(key)String token);

    @DELETE("penjualan/{idjual}")
    Call<Penjualan> deletePenjualan(@Path("idjual")String idjual,@Header(key) String token);


}
