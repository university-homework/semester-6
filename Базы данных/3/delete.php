<?php
$dsn = "pgsql:host=localhost;port=5432;dbname=db_task2;";
$pdo = new PDO($dsn, "postgres", "Ejdovfidjio11!");

$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$id = $_GET['id'];

$sql = "DELETE FROM teachers WHERE id = :id";
$stmt = $pdo->prepare($sql);
$stmt->bindParam(':id', $id, PDO::PARAM_INT);
$stmt->execute();

header("Location: index.php");
exit;
?>