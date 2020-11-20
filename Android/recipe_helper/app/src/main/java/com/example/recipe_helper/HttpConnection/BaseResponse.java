package com.example.recipe_helper.HttpConnection;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    public String status;
    public String errorMessage;
    public int errorCode;

    public int checkError(Context context) {
        if (context == null) return 1;
        String[] errorArray = status.split(":");
        status = errorArray[0];
        if (errorArray[0].equals("<success>")) {
            return 0;
        } else {
            errorCode = Integer.parseInt(errorArray[1]);
            errorMessage = errorArray[2];
            switch (errorCode) {
                case 1:
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return 1;
                case 2:
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return 2;
                case 3:
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return 3;
                case 4:
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return 4;
            }
        }
        return 1;
    }
}
