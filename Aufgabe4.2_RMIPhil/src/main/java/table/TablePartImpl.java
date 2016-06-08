package table;

import api.TablePart;

import java.util.UUID;

/**
 * Created by Fabian on 01.06.2016.
 */
public class TablePartImpl implements TablePart {

    private final String id = UUID.randomUUID().toString();

}
