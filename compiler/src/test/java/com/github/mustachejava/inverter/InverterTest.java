package com.github.mustachejava.inverter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.Node;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class InverterTest {

  @Test
  public void testParser() throws IOException {
    DefaultMustacheFactory dmf = new DefaultMustacheFactory();
    Mustache compile = dmf.compile("fdbcli.mustache");
    Path file = FileSystems.getDefault().getPath("src/test/resources/fdbcli.txt");
    String txt = new String(Files.readAllBytes(file), "UTF-8");
    Node invert = compile.invert(new Node(), txt, new AtomicInteger(0));
    System.out.println(invert);
  }

  @Test
  public void testSimple() throws IOException {
    DefaultMustacheFactory dmf = new DefaultMustacheFactory();
    Mustache test = dmf.compile(new StringReader("test {{value}} test"), "test");
    Node invert = test.invert(new Node(), "test value test", new AtomicInteger(0));
    Node node = new Node();
    node.put("value", asList(new Node("value")));
    assertEquals(node, invert);
  }

  @Test
  public void testIterable() throws IOException {
    DefaultMustacheFactory dmf = new DefaultMustacheFactory();
    Mustache test = dmf.compile(new StringReader("{{#values}}\ntest: {{value}}\n{{/values}}"), "test");
    Node invert = test.invert(new Node(), "test: sam\ntest: fred\n", new AtomicInteger(0));
    Node node = new Node();
    Node sam = new Node();
    sam.put("value", asList(new Node("sam")));
    Node fred = new Node();
    fred.put("value", asList(new Node("sam")));
    node.put("values", asList(sam, fred));
    assertEquals(node, invert);
  }
}
