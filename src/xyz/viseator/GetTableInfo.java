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
                for (int i = 0, rowInExcel, position = 1; i < 24; i++) {
                    if (i == 0 || i == 6 || i == 17) rowInExcel = -1;
                    else rowInExcel = position++;

                    if ((i >= 6 && i <= 9) || (i >= 10 && i <= 16))
                        tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_STRING_0_1, rowInExcel);
                    else if ((i >= 1 && i <= 5))
                        tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_NUMBER_0_1, rowInExcel);
                    else if (i == 0 || i == 17)
                        tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_STRING_1_2, rowInExcel);
                    else tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_NUMBER_1_2, rowInExcel);
                }
                break;
            case 2:
                tableInfo = new TableInfo(25);
                for (int i = 0, rowInExcel, position = 22; i < 25; i++) {
                    if (i == 11 || i == 22) rowInExcel = -1;
                    else rowInExcel = position++;

                    if (i == 11 || (i >= 14 && i <= 22))
                        tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_STRING_1_2, rowInExcel);
                    else tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_NUMBER_1_2, rowInExcel);
                }
                break;
            case 3:
                tableInfo = new TableInfo(26);
                for (int i = 0, rowInExcel, position = 45; i < 26; i++) {
                    rowInExcel = position++;
                    if (i == 25)
                        tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_STRING_0_1, rowInExcel);
                    else if (i == 20)
                        tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_STRING_1_2, rowInExcel);
                    else tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_NUMBER_1_2, rowInExcel);
                }
                break;
            case 4:
                tableInfo = new TableInfo(14);
                for (int i = 0,rowInExcel,position = 71; i < 14; i++) {
                    if (i == 1 || i == 12) rowInExcel = -1;
                    else rowInExcel = position++;
                    tableInfo.initRowInfo(i, TableInfo.DATA_TYPE_STRING_0_1, rowInExcel);
                }
                break;
            case 5:
                int rowInExcel = 83;
                tableInfo = new TableInfo(1);
                tableInfo.initRowInfo(0, TableInfo.DATA_TYPE_STRING_0_1, rowInExcel);
                break;
            default:
                tableInfo = new TableInfo(-1);
        }
        return tableInfo;
    }
}
