package com.prosperapp.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashear(String contrasenaPlana) {
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
    }

    public static boolean verificar(String contrasenaPlana, String contrasenaHash) {
        return BCrypt.checkpw(contrasenaPlana, contrasenaHash);
    }
}