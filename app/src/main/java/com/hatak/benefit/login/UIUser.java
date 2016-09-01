package com.hatak.benefit.login;

/**
 * Created by hatak on 22.08.16.
 */
public class UIUser {

    public static final String NO_VALUE = "";
    private final String cardNumber;
    private final String nikNumber;

    public UIUser(final String cardNumber, final String nikNumber) {
        this.cardNumber = cardNumber;
        this.nikNumber = nikNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getNikNumber() {
        return nikNumber;
    }

    public static UIUser createEmpty(){
        return new UIUser(NO_VALUE, NO_VALUE);
    }
}
