package Functional2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

public class Functional {
    public static void main(String []args) throws IOException {
        byte[] bytes;
        ArrayList<String> tokenList = new ArrayList<>();
        Stack<String> openTags = new Stack<>();

        bytes = Files.readAllBytes(Paths.get("alben.xml"));

        createTokenList(bytes, 0, tokenList);

        System.out.println(tokenList + "\n\n########################################### \n\n");

        parseFile(tokenList, openTags, 0);
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

    public static void getAlbumTitle(ArrayList<String> tokenList, int depth, int i) {
        String token = tokenList.get(i);

        if(token.equals("track")) {
            getAlbumTitle(tokenList, ++depth, ++i);
            return;
        } else if(token.equals("/track")) {
            getAlbumTitle(tokenList, --depth, ++i);
            return;
        } else if(token.equals("title") && depth == 1) {
            System.out.println("\tTitle:" + tokenList.get(i + 1));
            return;
        }

        getAlbumTitle(tokenList, depth, ++i);
    }

    public static void getInfos(ArrayList<String> tokenList, int i, String obj, String end) {
        String token = tokenList.get(i);

        if(token.equals(end)) {
            return;
        } else if(token.equals(obj)) {
            if(end.equals("/track")) System.out.print("\t");

            System.out.println("\t" + obj.substring(0, 1).toUpperCase() + obj.substring(1) + ":" + tokenList.get(i + 1));
        } else {
            getInfos(tokenList, ++i, obj, end);
        }
    }

    public static void getTracks(ArrayList<String> tokenList, int i) {
        String token = tokenList.get(i);

        if(token.equals("/album")) {
            return;
        } if(token.equals("track")) {
            getTrackInfo(tokenList, i);
        } else {
            getTracks(tokenList, ++i);
            return;
        }

        getTracks(tokenList, ++i);
    }

    public static void getTrackInfo(ArrayList<String> tokenList, int i) {
        String token = tokenList.get(i);

        if(token.equals("/track")) {
            return;
        } else {
            System.out.println("\tTrack:");
            getInfos(tokenList, i, "title", "/track");
            getInfos(tokenList, i, "length", "/track");
            getInfos(tokenList, i, "rating", "/track");
            System.out.print("\t\tFeatures:");
            ArrayList<String> list = new ArrayList<String>();
            getMultValues(tokenList, i, list, "feature");
            System.out.println(list);
            list.clear();
            System.out.print("\t\tWriting:");
            getMultValues(tokenList, i, list, "writing");
            System.out.println(list);
        }
    }

    public static ArrayList<String> getMultValues(ArrayList<String> tokenList, int i, ArrayList<String> list, String obj) {
        String token = tokenList.get(i);

        if(token.equals("/track")) {
            return list;
        } else if(token.equals(obj)) {
            list.add(tokenList.get(i + 1));
        }

        getMultValues(tokenList, ++i, list, obj);
        return list;
    }

    public static int findNextAlbum(ArrayList<String> tokenList, int i) {
        String token = tokenList.get(i);

        if(token.equals("/album")) {
            return i;
        } else {
            return findNextAlbum(tokenList, ++i);
        }
    }

    public static void parseFile(ArrayList<String> tokenList, Stack<String> openTags, int i) {
        if(i > tokenList.size()) {
            return;
        }

        System.out.println("Album:");

        int depth = 1;
        getAlbumTitle(tokenList, depth, i);
        getInfos(tokenList, i, "date", "/album");
        getInfos(tokenList, i, "artist", "/album");
        System.out.println("\tTracks: [");
        getTracks(tokenList, i);
        System.out.println("\t]");

        int end = findNextAlbum(tokenList, ++i);

        if(end  == tokenList.size() - 1) {
            return;
        } else {
            parseFile(tokenList, openTags, ++end);
        }
    }
}