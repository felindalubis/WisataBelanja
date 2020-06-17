package com.example.felindalubis.wisatabelanjagdss.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BobotModel implements Parcelable {
    public int id;
    public int rating;
    public int operasional;
    public int fasilitas;
    public int jarak;

    public BobotModel(int id, int rating, int fasilitas, int jarak, int operasional) {
        this.id = id;
        this.rating = rating;
        this.operasional = operasional;
        this.fasilitas = fasilitas;
        this.jarak = jarak;
    }

    public BobotModel() {
    }

    protected BobotModel(Parcel in){
        this.id = in.readInt();
        this.rating = in.readInt();
        this.fasilitas = in.readInt();
        this.jarak = in.readInt();
        this.operasional = in.readInt();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.rating);
        dest.writeInt(this.fasilitas);
        dest.writeInt(this.jarak);
        dest.writeInt(this.operasional);
    }

    public static final Creator<BobotModel> CREATOR = new Creator<BobotModel>(){

        @Override
        public BobotModel createFromParcel(Parcel source) {
            return new BobotModel(source);
        }

        @Override
        public BobotModel[] newArray(int size) {
            return new BobotModel[size];
        }
    };

}
