package Funktional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

public class Funktional {
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

    public static void parseFile(ArrayList<String> tokenList, Stack<String> openTags, int i) {
        if (i == tokenList.size() - 2) {
            return;
        }

        if(openTags.contains("album")) System.out.print("\t");
        if(openTags.contains("track")) System.out.print("\t");

        if(!tokenList.get(i).startsWith("/")) {
            String pushed = openTags.push(tokenList.get(i));
            //System.out.print("Push " + pushed);
        }

        if(tokenList.get(i + 2).equals("/" + tokenList.get(i))) {
            System.out.print(tokenList.get(i).substring(0, 1).toUpperCase() + tokenList.get(i).substring(1) +  ": " + tokenList.get(i + 1).substring(0, 1).toUpperCase() + tokenList.get(i + 1).substring(1) +  "\n");
            String tag = openTags.pop();
            //System.out.println("Pop " + tag);
            i += 3;
            parseFile(tokenList, openTags, i);
        } else {
            if(tokenList.get(i).startsWith("/")) {
                String tag = openTags.pop();

                //System.out.println("Pop " + tag);
                System.out.println();

                if(tag.equals("album")) {
                    i++;
                    parseFile(tokenList, openTags, i);
                    return;
                } else if(tag.equals("track") && isLast(tokenList, i, "track", false)) {
                    System.out.print("]\n" );
                }
            } else if(tokenList.get(i).equals("track")) {

                System.out.print("Tracks: [" + "\n");
//                i++;
//                parseFile(tokenList, openTags, i);
            } else {
                System.out.print(tokenList.get(i).substring(0, 1).toUpperCase() + tokenList.get(i).substring(1) + ": " + "\n");
            }

            i++;
            parseFile(tokenList, openTags, i);
        }
    }

    public static boolean isLast(ArrayList<String> tokenList, int i, String obj, boolean found) {
        if(tokenList.get(i).equals("album")) {
            found = false;
        } else if(tokenList.get(i).equals(obj)) {
            found = true;
            return found;
        } else {
            i++;
            found = isLast(tokenList, i, obj, found);
        }

        return found;
    }
}