<?php
try {
    $dsn = "pgsql:host=localhost;port=5432;dbname=db_task2;";
    $pdo = new PDO($dsn, "postgres", "Ejdovfidjio11!");

    $query = $pdo->query("SELECT * FROM teachers");
    $rows = $query->fetchAll(PDO::FETCH_ASSOC);

    foreach ($rows as $row) {
        print_r($row);
        echo "<br>";
    }
} catch (PDOException $e) {
    echo "Ошибка: " . $e->getMessage();
}
?>
