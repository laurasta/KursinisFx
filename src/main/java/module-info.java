//module com.example.kursinisfx {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires lombok;
//    requires java.persistence;
//    requires mysql.connector.java;
//    requires org.hibernate.orm.core;
//
//
//    opens com.example.kursinisfx to javafx.fxml;
//    exports com.example.kursinisfx;
//
//}

module com.example.kursinisfx {
        requires javafx.controls;
        requires javafx.fxml;
        requires lombok;
        requires java.persistence;
        requires mysql.connector.java;
        requires java.sql;
        requires org.hibernate.orm.core;
        requires java.naming;


        opens com.example.kursinisfx to javafx.fxml, org.hibernate.orm.core;
        exports com.example.kursinisfx;
        opens com.example.kursinisfx.model to org.hibernate.orm.core;
    exports com.example.kursinisfx.fxControllers;
    opens com.example.kursinisfx.fxControllers to javafx.fxml, org.hibernate.orm.core;

}