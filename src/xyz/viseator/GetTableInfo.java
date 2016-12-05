package xyz.viseator;

/**
 * Created by viseator on 2016/12/5.
 */
public class GetTableInfo {

    public static TableInfo get(int paperNum) {
        TableInfo tableInfo;
        switch (paperNum) {
            case 1:
                tableInfo = new TableInfo(24);
                for (int i = 0; i < 24; i++) {
                    if (i == 0 || (i >= 6 && i <= 8) || (i >= 10 && i <= 17))
                        tableInfo.initColumn(i, TableInfo.DATA_TYPE_STRING_0_1);
                    else if ((i >= 1 && i <= 5) || i == 9)
                        tableInfo.initColumn(i, TableInfo.DATA_TYPE_NUMBER_0_1);
                    else tableInfo.initColumn(i, TableInfo.DATA_TYPE_NUMBER_1_2);
                }
                break;
            case 2:
                tableInfo = new TableInfo(24);
                for (int i = 0; i < 24; i++) {
                    if (i == 11 || (i >= 14 && i <= 21) || (i >= 10 && i <= 17))
                        tableInfo.initColumn(i, TableInfo.DATA_TYPE_STRING_1_2);
                    else tableInfo.initColumn(i, TableInfo.DATA_TYPE_NUMBER_1_2);
                }
                break;
            case 3:
                tableInfo = new TableInfo(26);
                for (int i = 0; i < 26; i++) {
                    if (i == 25)
                        tableInfo.initColumn(i, TableInfo.DATA_TYPE_STRING_0_1);
                    else if (i == 20)
                        tableInfo.initColumn(i, TableInfo.DATA_TYPE_STRING_1_2);
                    else tableInfo.initColumn(i, TableInfo.DATA_TYPE_NUMBER_1_2);
                }
                break;
            case 4:
                tableInfo = new TableInfo(14);
                for (int i = 0; i < 14; i++) {
                    tableInfo.initColumn(i, TableInfo.DATA_TYPE_STRING_0_1);
                }
                break;
            case 5:
                tableInfo = new TableInfo(1);
                tableInfo.initColumn(0, TableInfo.DATA_TYPE_STRING_0_1);
                break;
            default:
                tableInfo = new TableInfo(-1);
        }
        return tableInfo;
    }
}
