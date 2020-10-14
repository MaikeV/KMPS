package Funktional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Funktional {
    public static void main(String []args) throws IOException {
        byte[] bytes;
        ArrayList<String> tokenList = new ArrayList<>();

        bytes = Files.readAllBytes(Paths.get("alben.xml"));
        System.out.println(bytes);

        createTokenList(bytes, 0, tokenList);

        System.out.println(tokenList);
    }

    public static void createTokenList(byte[] bytes, int currentPos, ArrayList<String> tokenList) {
        if(currentPos >= bytes.length) {
            return;
        }

        if(bytes[currentPos] == '\n' || bytes[currentPos] == '\r' || bytes[currentPos] == '\t') {
            currentPos++;

            createTokenList(bytes, currentPos, tokenList);
        } else if(new String(bytes, currentPos, 1, StandardCharsets.UTF_8).equals(new String("<")) || new String(bytes, currentPos, 1, StandardCharsets.UTF_8).equals(new String(">"))) {
            currentPos++;

            createTokenList(bytes, currentPos, tokenList);
        } else {
            int dataLength = findEndTag(currentPos, bytes, currentPos);;
            tokenList.add(new String(bytes, currentPos, dataLength, StandardCharsets.UTF_8));
            currentPos += dataLength;

            createTokenList(bytes, currentPos, tokenList);
        }
    }

    public static int findEndTag(int pos, byte[] bytes, int startPos) {
        if(new String(bytes, pos, 1, StandardCharsets.UTF_8).equals(new String("<")) || new String(bytes, pos, 1, StandardCharsets.UTF_8).equals(new String(">"))) {
            return pos - startPos;
        } else {
            pos++;
            return findEndTag(pos, bytes, startPos);
        }
    }
}