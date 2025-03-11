<?php
// Подключаемся к базе данных
$dsn = "pgsql:host=localhost;port=5432;dbname=db_task2;";
$pdo = new PDO($dsn, "postgres", "Ejdovfidjio11!");

$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$id = $_GET['id'];

$stmt = $pdo->prepare("SELECT * FROM teachers WHERE id = :id");
$stmt->bindParam(':id', $id, PDO::PARAM_INT);
$stmt->execute();
$teacher = $stmt->fetch(PDO::FETCH_ASSOC);

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $name = $_POST['name'];
    $subject = $_POST['subject'];

    $sql = "UPDATE teachers SET name = :name, subject = :subject WHERE id = :id";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':name', $name);
    $stmt->bindParam(':subject', $subject);
    $stmt->bindParam(':id', $id);
    $stmt->execute();

    header("Location: index.php");
    exit;
}
?>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактировать учителя</title>
</head>
<body>
    <h2>Редактировать учителя</h2>
    <form action="edit.php?id=<?php echo $teacher['id']; ?>" method="POST">
        <label for="name">Имя:</label>
        <input type="text" id="name" name="name" value="<?php echo $teacher['name']; ?>" required><br>

        <label for="subject">Предмет:</label>
        <input type="text" id="subject" name="subject" value="<?php echo $teacher['subject']; ?>" required><br>

        <button type="submit">Обновить</button>
    </form>
</body>
</html>