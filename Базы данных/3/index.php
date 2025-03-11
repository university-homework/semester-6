<?php
$dsn = "pgsql:host=localhost;port=5432;dbname=db_task2;";
$pdo = new PDO($dsn, "postgres", "Ejdovfidjio11!");

$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$stmt = $pdo->query("SELECT * FROM teachers");
$teachers = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список учителей</title>
</head>
<body>
    <h2>Список учителей</h2>
    <ul>
        <?php foreach ($teachers as $teacher): ?>
            <li>
                <?php echo $teacher['name'] . " - " . $teacher['subject']; ?>
                <a href="edit.php?id=<?php echo $teacher['id']; ?>"><button>Редактировать</button></a>
                <a href="delete.php?id=<?php echo $teacher['id']; ?>" onclick="return confirm('Вы уверены?')"><button>Удалить</button></a>
            </li>
        <?php endforeach; ?>
    </ul>

    <a href="add.php"><button>Добавить нового учителя</button></a>
</body>
</html>