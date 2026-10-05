package com.example.scpractice.Utils;

import com.example.scpractice.enums.Language;

public class MarathiUtils implements LanguageUtils {

    public static String getPronunciation(Language language, int num) {
        return getPronunciation(language, num, 0);
    }

    public static String getPronunciation(Language language, int num, int type) {
        String local = null;
        if (type == 0) {
            if (language.equals(Language.MARATHI)) {
                switch (num) {
                    case 1: {
                        local = "Ek";
                        break;
                    }
                    case 2: {
                        local = "Dune";
                        break;
                    }
                    case 3: {
                        local = "Trik";
                        break;
                    }
                    case 4: {
                        local = "Chau";
                        break;
                    }
                    case 5: {
                        local = "Panche";
                        break;
                    }
                    case 6: {
                        local = "Sakam";
                        break;
                    }
                    case 7: {
                        local = "Sati";
                        break;
                    }
                    case 8: {
                        local = "aathi";
                        break;
                    }
                    case 9: {
                        local = "Nahu";
                        break;
                    }
                    case 10: {
                        local = "Daahe";
                        break;
                    }

                    default: {
                        local = "EK";
                        break;
                    }
                }
            }
        } else if (type == 1) {

            if (language.equals(Language.MARATHI)) {
                switch (num) {
                    case 2: {
                        local = "Bay";
                    }
                }

            }

        }

        return local;

    }
}
