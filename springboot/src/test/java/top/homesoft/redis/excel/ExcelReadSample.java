package top.homesoft.redis.excel;

import com.github.liaochong.myexcel.core.SaxExcelReader;
import top.homesoft.java.excel.CommissionData;
import top.homesoft.java.utils.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExcelReadSample {


    public static void main(String[] args) throws Exception {
        //URL htmlToExcelEampleURL = this.getClass().getResource("/templates/read_example.xlsx");
        Path path = Paths.get("C:\\Users\\ADMIN\\Desktop\\withdraw.csv");
        String charset = FileUtil.resolveCode("C:\\Users\\ADMIN\\Desktop\\common2.csv");
        List<CommissionData> csvPeoples = SaxExcelReader.of(CommissionData.class)
                .charset(charset)
                .rowFilter(row -> row.getRowNum() > 0).read(path.toFile());
        System.out.println(csvPeoples.size());

        String sql = "\n" +
                "  INSERT INTO t_withdraw_apply(\"ID\",\n" +
                "\t\"MID\",\"REALNAME\", \"SET_NAME\",\n" +
                "\t\"BANK_CARD_NO\", \"BANK_NAME\",\n" +
                "\t\"APPLY_AMOUNT\", \n" +
                "\t\"APPLY_TIME\", \n" +
                "\t\"AUDIT_TIME\",\n" +
                "\t\"REMIT_TIME\",\n" +
                "\t\"SETUP_ID\",\n" +
                "\t\"APPLY_STATUS\")\n" +
                "\t\n" +
                "select (select seq+1 from T_SEQUENCE where tablename like 't_withdraw_apply'),\n" +
                " mid,m.name,s.SET_NAME,\n" +
                " m.BANKACCOUNT,m.bank,\n" +
                " %d,to_date('2019-10-15 10:00:00','yyyy-mm-dd HH24:mi:ss'),\n" +
                " to_date('2019-10-15 10:02:00','yyyy-mm-dd HH24:mi:ss'),\n" +
                " to_date('2019-10-15 10:05:00','yyyy-mm-dd HH24:mi:ss')\n" +
                " ,s.set_id, 2 from t_setup  s ,t_member m\n" +
                " where s.set_id = m.BINDING_SETUP \n" +
                " and SET_NAME like '%s'";

        for (int i = 0; i < csvPeoples.size(); i++) {
            CommissionData data = csvPeoples.get(i);
            String.format(sql, data.getMoney(), data.getName());
        }
    }

}
