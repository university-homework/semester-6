<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $name = $_POST['name'];
    $subject = $_POST['subject'];

    $dsn = "pgsql:host=localhost;port=5432;dbname=db_task2;";
    $pdo = new PDO($dsn, "postgres", "Ejdovfidjio11!");
    
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $sql = "INSERT INTO teachers (name, subject) VALUES (:name, :subject)";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':name', $name);
    $stmt->bindParam(':subject', $subject);
    $stmt->execute();

    header("Location: index.php");
    exit;
}
?>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Добавить учителя</title>
</head>
<body>
    <h2>Добавить нового учителя</h2>
    <form action="add.php" method="POST">
        <label for="name">Имя:</label>
        <input type="text" id="name" name="name" required><br>

        <label for="subject">Предмет:</label>
        <input type="text" id="subject" name="subject" required><br>

        <button type="submit">Добавить</button>
    </form>
</body>
</html>
