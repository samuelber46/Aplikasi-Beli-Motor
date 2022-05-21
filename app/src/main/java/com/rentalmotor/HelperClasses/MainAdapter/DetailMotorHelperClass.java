package com.rentalmotor.HelperClasses.MainAdapter;

public class DetailMotorHelperClass {
    String id,image,namaMotor,volume,harga;

    public DetailMotorHelperClass(String id, String image, String namaMotor, String volume, String harga) {
        this.id = id;
        this.image = image;
        this.namaMotor = namaMotor;
        this.volume = volume;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getNamaMotor() {
        return namaMotor;
    }

    public String getVolume() {
        return volume;
    }

    public String getHarga() {
        return harga;
    }
}
