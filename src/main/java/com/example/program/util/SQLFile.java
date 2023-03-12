package com.example.program.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLFile implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SQLFile.class);

    private final Properties queries = new Properties();

    /**
     * Constructs a SQL file with the specified input stream
     * @param is Input stream representing the SQL file
     */
    public SQLFile(InputStream is) {
        parseFile(is);
        if (LOG.isInfoEnabled()) {
            logConfiguration();
        }
    }

    private void parseFile(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            String queryName = null;
            List<String> queryLines = null;
            while ((line = nextLine(reader)) != null) {
                if (isQueryName(line)) {
                    registerQuery(queryName, queryLines);
                    queryName = extractQueryName(line);
                    queryLines = new ArrayList<>();
                } else {
                    // Query line
                    if (queryLines != null) {
                        queryLines.add(line);
                    }
                }
            }
            registerQuery(queryName, queryLines);
        } catch (IOException e) {
            throw new IllegalArgumentException("SQL file could not be read", e);
        }
    }

    private void registerQuery(String queryName, List<String> queryLines) {
        if (queryName != null && !queryName.trim().isEmpty() && queryLines != null && !queryLines.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String queryLine: queryLines) {
                sb.append(queryLine).append('\n');
            }
            queries.setProperty(queryName, sb.substring(0, sb.length() - 1));
        }
    }

    private String extractQueryName(String line) {
        Matcher matcher = Pattern.compile("#(\\w+)").matcher(line);
        return matcher.find() ? matcher.group(1) : null;
    }

    private boolean isQueryLine(String line) {
        return !line.trim().isEmpty() && !line.matches("^--.*");
    }

    private boolean isQueryName(String line) {
        return line.matches("^--\\s*#\\w+");
    }

    /**
     * Reads next valid line from file
     * @param reader File reader
     * @return Next valid line, or <code>null</code> if EOF.
     * @throws IOException If the file could not be read
     */
    private String nextLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
        } while (line != null && !isValidLine(line) );
        return line;
    }

    private boolean isValidLine(String line) {
        return isQueryLine(line) || isQueryName(line);
    }

    /**
     * Names of all queries valid in the context of this SQL file.
     *
     * @return Set of query names declared on the SQL file
     */
    public Set<String> queryNames() {
        return queries.stringPropertyNames();
    }

    /**
     * Gets the query associated with the provided query name.
     * @param name Name of the query to be retrieved
     * @return The query identified by <code>name</code>, or <code>null</code> if
     * <code>!this.queryNames().contains(name)</code>
     */
    public String query(String name) {
        return queries.getProperty(name);
    }

    private void logConfiguration() {
        SortedSet<String> queryNames = new TreeSet<>(queryNames());
        LOG.info("{} {} initialized: {}", queryNames.size(), queryNames.size() > 1 ? "queries" : "query" , queryNames);
    }
}
