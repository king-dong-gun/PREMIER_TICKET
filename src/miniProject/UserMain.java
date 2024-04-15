package miniProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static miniProject.UserUtils.*;

public class UserMain {
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String USER = "adam";
    static final String PASS = "1234";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // 사용자로부터 회원 정보 입력 받기
            String userId;
            String userPass;
            String userName;
            String email;
            String phoneNumber;
            while (true) {
                userId = inPutId(sc, stmt); // 아이디 입력
                if (isUserIdExists(stmt, userId)) {
                    System.out.println("이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
                    continue;
                }
                userPass = inputPassword(sc); // 비밀번호 입력
                if (userPass.length() < 6) {
                    System.out.println("비밀번호 길이가 올바르지 않습니다. (최소 6자 이상)");
                    continue;
                } else {
                    System.out.println("비밀번호 입력이 완료되었습니다.");
                }

                System.out.println("이름을 입력하세요 >> ");
                userName = sc.nextLine();
                System.out.println("이메일을 입력하세요 (형식 email@address.com) >> ");
                email = sc.nextLine();
                System.out.println("전화번호를 입력하세요 >> ");
                phoneNumber = sc.nextLine();

                // 입력된 전화번호의 유효성 검사
                if (!isValidNumber(phoneNumber)) {
                    System.out.println("유효하지 않은 전화번호 형식입니다. 다시 입력해주세요.");
                    continue;
                }

                // 모든 정보가 유효한 경우 회원가입 완료
                break;
            }

            // SQL 쿼리를 사용하여 userinfo 테이블에 회원 정보 삽입
            String sql = "INSERT INTO USERINFO (UserID, Password, Name, Email, PhoneNumber) " +
                    "VALUES ('" + userId + "', '" + userPass + "', '" + userName + "', '" + email + "', '" + phoneNumber + "')";
            stmt.executeUpdate(sql);

            System.out.println("회원가입이 완료되었습니다.");
            System.out.println("ID >> : " + userId);
            System.out.println("이름 >> " + userName);
            System.out.println("이메일 >> " + email);
            System.out.println("전화번호 >> " + phoneNumber);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

}
