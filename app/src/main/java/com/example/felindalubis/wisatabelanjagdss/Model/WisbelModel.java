package com.example.felindalubis.wisatabelanjagdss.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class WisbelModel implements Parcelable {
    public int id;
    public String nama_tempat;
    public String jenis;
    public double rating;
    public String alamat;
    public String jam_buka;
    public String jam_tutup;
    public double jam_operasional;
    public int jml_fasilitas;
    public double lat;
    public double lng;
    public String foto;
    public double jarak;
    public String fasilitas;

    public WisbelModel(int id, String nama_tempat, Double rating, String jam_buka, String jam_tutup, int jml_fasilitas, double lat, double lng, String jenis, String alamat, double jam_operasional, String foto, double jarak, String fasilitas) {
        this.id = id;
        this.nama_tempat = nama_tempat;
        this.jenis = jenis;
        this.rating = rating;
        this.alamat = alamat;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
        this.jam_operasional = jam_operasional;
        this.jml_fasilitas = jml_fasilitas;
        this.lat = lat;
        this.lng = lng;
        this.foto = foto;
        this.jarak = jarak;
        this.fasilitas = fasilitas;
    }

    public WisbelModel(int id, String nama_tempat, Double rating, String jam_buka, String jam_tutup, int jml_fasilitas, double lat, double lng, String jenis, String alamat, double jam_operasional, String foto, String fasilitas) {
        this.id = id;
        this.nama_tempat = nama_tempat;
        this.jenis = jenis;
        this.rating = rating;
        this.alamat = alamat;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
        this.jam_operasional = jam_operasional;
        this.jml_fasilitas = jml_fasilitas;
        this.lat = lat;
        this.lng = lng;
        this.foto = foto;
        this.fasilitas = fasilitas;
    }


    public WisbelModel() {
    }

    protected WisbelModel(Parcel in){
        this.id = in.readInt();
        this.nama_tempat = in.readString();
        this.jenis = in.readString();
        this.rating = in.readDouble();
        this.alamat = in.readString();
        this.jam_buka = in.readString();
        this.jam_tutup = in.readString();
        this.jam_operasional = in.readDouble();
        this.jml_fasilitas = in.readInt();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.foto = in.readString();
        this.jarak = in.readDouble();
        this.fasilitas = in.readString();
    }

    //    public WisbelModel(int id, String nama_tempat, double rating, String jam_buka, String jam_tutup, int jml_fasilitas, String jenis, String alamat, double jam_operasional, String foto) {
//        this.setId(id);
//        this.setNama_tempat(nama_tempat);
//        this.setJenis(jenis);
//        this.setRating(rating);
//        this.setAlamat(alamat);
//        this.setJam_buka(jam_buka);
//        this.setJam_tutup(jam_tutup);
//        this.setJam_operasional(jam_operasional);
//        this.setJml_fasilitas(jml_fasilitas);
////        this.setLat(lat);
////        this.setLng(lng);
//        this.setFoto(foto);
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nama_tempat);
        dest.writeString(this.jenis);
        dest.writeDouble(this.rating);
        dest.writeString(this.alamat);
        dest.writeString(this.jam_buka);
        dest.writeString(this.jam_tutup);
        dest.writeDouble(this.jam_operasional);
        dest.writeInt(this.jml_fasilitas);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.foto);
        dest.writeDouble(this.jarak);
        dest.writeString(this.fasilitas);
    }

    public static final Parcelable.Creator<WisbelModel> CREATOR = new Parcelable.Creator<WisbelModel>(){

        @Override
        public WisbelModel createFromParcel(Parcel source) {
            return new WisbelModel(source);
        }

        @Override
        public WisbelModel[] newArray(int size) {
            return new WisbelModel[size];
        }
    };

}
