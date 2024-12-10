/*
 * @ (#) getData.java       1.0     02/10/2024
 *
 *Copyright (c) 2024 IUH. All right reserved.
 */
package controller;

import entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * @description:
 * @author: Hiep Nguyen
 * @date:   02/10/2024
 * version: 1.0
 */
public class getData {
    public static NhanVien nv;
    public static HoaDon hd;
    public static KhachHang kh;
    public static LichTrinh lt;
    public static LichTrinh ltkh;
    public static ArrayList<Ve> dsve;
    public static ArrayList<ChiTietLichTrinh> dsctlt;
    public static ArrayList<ChiTietLichTrinh> dsctltkh;
    public static ArrayList<ChiTietHoaDon> dscthd;
    public static LocalDateTime gioMoCa;
    public static HashMap<String, Double> mapLePhi;
    public static double tienDauCa;
    public static String ghiChu;
}
