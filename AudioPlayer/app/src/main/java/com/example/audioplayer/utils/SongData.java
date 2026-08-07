package com.example.audioplayer.utils;

import java.util.Arrays;
import java.util.List;

public class SongData {

    // Song model class
    public static class Song {
        private String title;
        private String link;

        public Song(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        @Override
        public String toString() {
            return "Song{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }

    public static final List<Song> ALL_SONGS = Arrays.asList(
           new Song("Brown Rang", "https://drive.google.com/file/d/1CucDvxbiLEU5UsfzDKvWi_x7GD4_Wz1V/view?usp=drivesdk"),
            new Song("Ek Tera Naam Hai Saaza", "https://drive.google.com/file/d/1CwyQQLHeRP_M_fmFnu78wCG8NOm3V54L/view?usp=drivesdk"),
            new Song("Nashe shi Chad Gyi", "https://drive.google.com/file/d/1D109bwYgv9jKuvYwB5uGoiT8BJi-C1UQ/view?usp=drivesdk"),
            new Song("Saiyara Main Saiyara", "https://drive.google.com/file/d/1D6uRAPYx4E62cp8o3GFmtQ52prHkxLfg/view?usp=drivesdk"),
            new Song("Mere Haath me Tera Haath Ho Fanah", "https://drive.google.com/file/d/1D7x0AmyWilyUjOA4MqSlhQ_EzQV-iF_U/view?usp=drivesdk"),
            new Song("Moh Moh Ke Daage", "https://drive.google.com/file/d/1D9k7nrpSvELxmyPxjqqP3IgQZwQkYEkW/view?usp=drivesdk"),
            new Song("Pushpa 2 - Ambarsa Lagta Hai Mera Saami", "https://drive.google.com/file/d/1DEdv7C_buuehyv0ASGU7sdwypsiR8rfh/view?usp=drivesdk"),
            new Song("Bol Halke Halke", "https://drive.google.com/file/d/1DFWiDtWYHbaD-ByZ9vgG2Mn0HB3lXxCj/view?usp=drivesdk"),
            new Song("Pushpa- More Saami Saami", "https://drive.google.com/file/d/1DJArgf5IvjnTu2AhtZ3IwaRHA8Y7c5IT/view?usp=drivesdk"),
            new Song("Haan Hasi Ban Gaye", "https://drive.google.com/file/d/1DNY-cV8l6Ox0djbd2Ki6Tm-OclNLG7yI/view?usp=drivesdk"),
            new Song("Hum Ko Hamise Chura Lo", "https://drive.google.com/file/d/1EzJ4t2pK_zaNOtJz6SNk01EElJPBvZlR/view?usp=drivesdk"),
            new Song("Hamari Adhuri Kahani", "https://drive.google.com/file/d/1KiTre8abMoXcUOsuRY5JYE4SA7NyxSo7/view?usp=drivesdk"),
            new Song("Kahin Pyar Na Ho jaye", "https://drive.google.com/file/d/1M2kqd-_Wm7SY8R7E9Ue3EajDd-WsPhWk/view?usp=drivesdk"),
            new Song("Lekin Muskaan Ho aiesi", "https://drive.google.com/file/d/1H1uEu1fHQG2RzOV_xz3brhTZ4QNsprSl/view?usp=drivesdk"),
            new Song("Chura Ke Dil Mera", "https://drive.google.com/file/d/1MKHk6sAOlr4pNolMQSNcO7E8PBJsjVCK/view?usp=drivesdk"),
            new Song("Dil Diya Galla", "https://drive.google.com/file/d/1H7FE5sKFQIjOdCcSQyrQGLjqyLqUrnEQ/view?usp=drivesdk"),
            new Song("Ae Mere Hamsafar", "https://drive.google.com/file/d/1MhqnX-5WSwkz6Z4LmFojT-xjtjeKurri/view?usp=drivesdk"),
            new Song("Chand Se Sifaarish Jo Karta Hamari - Fanah", "https://drive.google.com/file/d/1FH9wwd_ROY3TxSqj5tzCWq7ci7cjbvP2/view?usp=drivesdk"),
            new Song("Dil Ibaadat Kr raha Hai", "https://drive.google.com/file/d/1Kf_Y5sHAb6KpvgVgd_g2pUZsaeaG-pkI/view?usp=drivesdk"),
            new Song("Dil Ne Ye Kaha Hai Dil Se", "https://drive.google.com/file/d/1MmFz9JWY-A_UW4OgHznW4zdKo1cto3yW/view?usp=drivesdk"),
            new Song("Lambi Judai", "https://drive.google.com/file/d/1L6Zg728_O154oLa92dgM69rlVIN5Igd7/view?usp=drivesdk"),
            new Song("AAya vhi tera seriya wala", "https://drive.google.com/file/d/1L1FnjXJ-sJrSkcQMFvpGnmBH_QrxmBJE/view?usp=drivesdk"),
            new Song("Haan Hasi Ban Gaye (Male)", "https://drive.google.com/file/d/1Kag1f9lW0Mt6IhCj7gpIHZHKW89JQTbs/view?usp=drivesdk"),
            new Song("Ek din app Humko Mil Jayenge Socha na tha", "https://drive.google.com/file/d/1MRiMtAnIz8Q-X7ZI5B31imzsGg7kBVjQ/view?usp=drivesdk"),
            new Song("Na Kajre ki Dhaar ", "https://drive.google.com/file/d/1MOR4I9Qworq_bM_uUWVUI9jXaglEh8k-/view?usp=drivesdk"),
            new Song("Khamakha Kwab Bunta Raha", "https://drive.google.com/file/d/1ELzKGjydXidCtWik8f3eKNA8wmAO3ths/view?usp=drivesdk"),
            new Song("Aaila Re aaila", "https://drive.google.com/file/d/1K8mZ4FTDYfQlG2ND4bUordKBUPPUaw2z/view?usp=drivesdk"),
            new Song("Rim zim Rim Zim", "https://drive.google.com/file/d/1FcAM0oa5AgsXX4r0SJhN1To5boRo357I/view?usp=drivesdk"),
            new Song("Aur kuch Bair Mujhe", "https://drive.google.com/file/d/1LDi_NQMeBIMFCpx3Co0V6QrBKYuiyqad/view?usp=drivesdk"),
            new Song("O Re Piya", "https://drive.google.com/file/d/1FonPvSFaCX3TiqwS_c9_ZUzH0TbUVzhh/view?usp=drivesdk"),
            new Song("Desh Rangila", "https://drive.google.com/file/d/1FXpR9gDg1VNvc7kxV6Xbt4yOULW8UgyL/view?usp=drivesdk"),
            new Song("Jannat Jaha", "https://drive.google.com/file/d/1LMPkeTpC_EkEPZItGKNu1iOONpJzGJTn/view?usp=drivesdk"),
            new Song("Wada Raha Sanam", "https://drive.google.com/file/d/1MQMXEQw6SWCs2nB6QtnNTd_O2OcCqBwH/view?usp=drivesdk"),
            new Song("Fimi Hai Tera Mera Pyar", "https://drive.google.com/file/d/1G-yl4m2Vwh2YAPYpBQFi8hXjkENrehZ6/view?usp=drivesdk"),
            new Song("Tuz se Mera Deen Dharam Tuzse Meri Duhai", "https://drive.google.com/file/d/1EVUiiicQ4CT2--RGQvsD-nB08fyXRlVL/view?usp=drivesdk"),
            new Song("Lambi Judai", "https://drive.google.com/file/d/1L9xSrwEivSKc82byUa1XBAZldwU-IIRw/view?usp=drivesdk"),
            new Song("Papa Kahte hai Bada Naam Krega", "https://drive.google.com/file/d/1MUh3YjrU5pJ7NbpXS3n1gFaZS0XEfuf-/view?usp=drivesdk"),
            new Song("Tu Jaha Main Waha", "https://drive.google.com/file/d/1EKCptXDReN_r207m6s1DxUoGdo0cTo5W/view?usp=drivesdk"),
            new Song("Aadmi Khilona Hai", "https://drive.google.com/file/d/1MOh9slW_eCK97-zO0jo9U5N4QihliH_4/view?usp=drivesdk"),
            new Song("Agar Tum Mil Jao", "https://drive.google.com/file/d/1KegC-BZaSM7Ukw0i4nMqeMNp5LbuKfQh/view?usp=drivesdk"),
            new Song("Tum Baarsh ke mausam me", "https://drive.google.com/file/d/1JbZD_5pk1M4WsTCOWt6XdCybyaB0T-7_/view?usp=drivesdk"),
            new Song("Kahin ye tere dil se chup chup ke", "https://drive.google.com/file/d/1FgMmMn3MTuNCaRvXN15N8rSWSNlngGsh/view?usp=drivesdk"),
            new Song("Dil Jisk jinda hai O tum ho", "https://drive.google.com/file/d/1IRdQ73XV16Njuk3RakytGRxQEoSnAfCM/view?usp=drivesdk"),
            new Song("Do Pal ka khwabo ka kaarwaan", "https://drive.google.com/file/d/1ERG2KGpDw3ZlxLPpNlpEEsfdSBkTV5ej/view?usp=drivesdk"),
            new Song("door ankhiyonse jaye na", "https://drive.google.com/file/d/1I0MAND49KAzd67AN3f1Y5klcgnZNyXfH/view?usp=drivesdk"),
            new Song("Gulabi Saadi", "https://drive.google.com/file/d/1JzIY1VhKq-gjaqSJAY6sPkzLNPP-3PvP/view?usp=drivesdk"),
            new Song("Ha Tu Hai Ha Tu Hai", "https://drive.google.com/file/d/1LQGmZva4w5CFvIuh3ab25ATBoe4ktbwf/view?usp=drivesdk"),
            new Song("halki Halki si Barsat aa gayi", "https://drive.google.com/file/d/1JdLkNbpI8ayGICvrntdQt3XkkzilfHIt/view?usp=drivesdk"),
            new Song("Hole Hole", "https://drive.google.com/file/d/1EulwKPmv6HjNS_FLWAj73InyPoJROEHt/view?usp=drivesdk"),
            new Song("Heeriyen Dil Janiye ", "https://drive.google.com/file/d/1JCG5ACkkuZH3cmlKeIOMvnKdjHGXB2Ko/view?usp=drivesdk"),
            new Song("Ye Sona Ye Sona", "https://drive.google.com/file/d/1EznOkh3qnxp7gK4sk1_Yz_OX7GbFuLfE/view?usp=drivesdk"),
            new Song("Kaisa Ye Ishq Hai Ajab sa risk hai", "https://drive.google.com/file/d/1EmrZrrDHU2gZUaQOoNt5iLW9HKDy-zM3/view?usp=drivesdk"),
            new Song("You Are My Sonia", "https://drive.google.com/file/d/1nn9xrhCF_G519pw3a0Ir9CNnLP81Q-6W/view?usp=drivesdk")
    );

    public static final List<Song> MY_FAVORITE_SONGS = Arrays.asList(
            new Song("Jag ghumia", "https://drive.google.com/file/d/1GNBE80geq1_fAjfHUwyRpquHKJsERvN0/view?usp=drivesdk"),
            new Song("Zihal-e-miskin", "https://drive.google.com/file/d/1NAydqbYDLwqQsszBuqrhssKcMuV3oO4q/view?usp=drivesdk"),
            new Song("Jita tha jiske liye", "https://drive.google.com/file/d/1MPhTS-LtJSJW1ZClxdnNsrihByXdoMSz/view?usp=drivesdk"),
            new Song("Kamaria dole bhojpuri", "https://drive.google.com/file/d/1Jjci3NqpV9XTCgtD7OZQV7UH3dexhHnj/view?usp=drivesdk"),
            new Song("Kya hua Tera Wada", "https://drive.google.com/file/d/1JPbpprfUYJqlhSGIg2el9FUZrHGZzsiU/view?usp=drivesdk"),
            new Song("Meri Aashiqui", "https://drive.google.com/file/d/1HGjot5BLXuHvO-kaJo8xALIot1kAYwFL/view?usp=drivesdk"),
            new Song("Mirchi Divine", "https://drive.google.com/file/d/1LouS0sTX6_ZoZcih14mazbcvZpoASVnq/view?usp=drivesdk"),
            new Song("Param Sundari", "https://drive.google.com/file/d/1KHUu0IxsN7b8_1JaREvw7ycgmH-JCJUV/view?usp=drivesdk"),
            new Song("Peen Loo", "https://drive.google.com/file/d/1KlJ4WyrMpePrasxdEbDcm_XwBqByykCY/view?usp=drivesdk"),
            new Song("Peena Chod Denge", "https://drive.google.com/file/d/1Hdrq4NfqhtSEk7Nov5-oeZLPeZK9h--a/view?usp=drivesdk"),
            new Song("Dil Shambhala Ja Jara", "https://drive.google.com/file/d/1KeFZzrvvceYp4_0KCLtQ6SlKOXf50aqS/view?usp=drivesdk"),
            new Song("Rabba Mai to Mar Gaya vhe", "https://drive.google.com/file/d/1KupKZFF292fekiPNsLaWhIbAfPvqiY0W/view?usp=drivesdk"),
            new Song("Saans", "https://drive.google.com/file/d/1G41VdpYNwpDFll2EhYiPZfZh-ab78YT3/view?usp=drivesdk"),
            new Song("Saawan me Lag gayi aag", "https://drive.google.com/file/d/1LsTj4f2-HZy0JLUp1KghYzKV-rfmbVv1/view?usp=drivesdk"),
            new Song("Tere bin X sajni Re", "https://drive.google.com/file/d/1K8KUmtKpZjI_WP5ptjdcr4jk73NGuwfi/view?usp=drivesdk"),
            new Song("Tere Liye", "https://drive.google.com/file/d/1GvF-iLwdffG8kUeKQQErKu-cc-5Hbz_M/view?usp=drivesdk"),
            new Song("Tujh mein rab dikhta Hai", "https://drive.google.com/file/d/1H4hedryo8p6wCYNXFCYnRg45NoJgJ2sS/view?usp=drivesdk"),
            new Song("Woh Ladki Bahut Yaad Aati hai", "https://drive.google.com/file/d/1MmxasNaBFpk7KRz8BlQ2yc5ZDXtmeC_7/view?usp=drivesdk"),
            new Song("Zara sa", "https://drive.google.com/file/d/1LRGDjipaJJ_eQdPinuStpK4jYpfbl9v0/view?usp=drivesdk"),
            new Song("Urvashi", "https://drive.google.com/file/d/1fgPyw9aKXIXmYDCHwbbkKOx5_jqVjFR1/view?usp=drivesdk"),
            new Song("You are my sonia", "https://drive.google.com/file/d/1fm6qYH57QledA3QjhT_8Ffd_l5_LB-3R/view?usp=drivesdk")
    );

    public static final List<Song> BHAKTI_SONGS = Arrays.asList(
            new Song("Hai Katha Sangram Ki ", "https://drive.google.com/file/d/1qJ2H41yyp1ysqEubCbh6Cl7uGfUuVVDw/view?usp=drivesdk"),
            new Song("Namo Namo He Shankara ", "https://youtu.be/dx4Teh-nv3A?si=dMFBVvvenMuvWZbx"),
            new Song("Mauli Mauli", "https://drive.google.com/file/d/10gGEhV7a2jyXX7ohOYwYbu55szUd0p43/view?usp=drivesdk"),
            new Song("Mere Baba", "https://drive.google.com/file/d/10cPa_6WNM2Cc_crtE-0QWL44xIBFY62O/view?usp=drivesdk"),
            new Song("Kal Ratri Hey Kalyani", "https://drive.google.com/file/d/10dVpHqWf-VEoV9rNqtlUJahqtEAjEIWZ/view?usp=drivesdk"),
            new Song("Kee Jo Keshri ke laal", "https://drive.google.com/file/d/10m-m1sx69jsS9eeBKoeXv2dkL35UM-1n/view?usp=drivesdk"),
            new Song("Mere Shankara", "https://drive.google.com/file/d/10qGkE85BXM1AiCmWyGZMPlbmMeW018uw/view?usp=drivesdk")
    );
}
