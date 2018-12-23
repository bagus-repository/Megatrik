package id.co.ardata.megatrik.customer.utils;

import android.util.Patterns;

public class RegexValidation {
    public static final String PERSON_NAME = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
    public static final String PASSWORD = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
    public static final String EMAIL = Patterns.EMAIL_ADDRESS.toString();
}
