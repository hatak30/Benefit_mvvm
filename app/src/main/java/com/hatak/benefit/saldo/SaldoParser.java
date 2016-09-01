package com.hatak.benefit.saldo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hatak on 30.08.16.
 */
public class SaldoParser {

    public static String getSaldoValue(String html) {
        String money = "---";
        Pattern pattern = Pattern.compile("\\Dostępne saldo: <strong>(.*?)\\</strong><br /><br />");
        Matcher m = pattern.matcher(html);
        while (m.find()) {
            money = m.group(1);
        }
        return money;
    }

    public static List<Transaction> getTransactions(String html) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("uneven");
        for (Element element : elements) {
            Transaction t = new Transaction();
            int index = 0;
            List<Node> nodes = element.childNodes();
            for (Node node : nodes) {
                List<Node> subNodes = node.childNodes();
                for (Node subNode : subNodes) {
                    if (subNode instanceof TextNode) {
                        TextNode textNode = (TextNode) subNode;
                        if (index == 0) {
                            t.setDate(textNode.getWholeText());
                        } else if (index == 1) {
                            t.setType(textNode.getWholeText());
                        } else if (index == 2) {
                            t.setPrice(textNode.getWholeText());
                        } else if (index == 3) {
                            t.setPlace(textNode.getWholeText());
                        }
                        index++;
                    }
                }
            }
            transactions.add(0, t);
        }
        return transactions;
    }
}
