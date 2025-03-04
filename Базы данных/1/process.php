<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = htmlspecialchars($_POST["name"]);
    $email = htmlspecialchars($_POST["email"]);
    $subscriber = htmlspecialchars($_POST["subscriber"]);
    $not_subscriber = htmlspecialchars($_POST["not_subscriber"]);
    $born_date = htmlspecialchars($_POST["born_date"]);
    $message = htmlspecialchars($_POST["message"]);

    echo "<h2>Вы ввели:</h2>";
    echo "имя: " . $name . "<br>";
    echo "email: " . $email . "<br>";
    echo "subscriber: " . $subscriber . "<br>";
    echo "not_subscriber: " . $not_subscriber . "<br>";
    echo "born_date: " . $born_date . "<br>";
    echo "message: " . $message . "<br>";
} else {
    echo "Некорректный запрос!";
}
?>