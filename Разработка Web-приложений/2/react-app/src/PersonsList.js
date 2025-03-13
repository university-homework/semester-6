import React from 'react';

const data = {
  header: 'Информация о пожертвованиях',
  persons: [
    {id: 0, name: 'Иван', surname: 'Иванов', donate: 300},
    {id: 1, name: 'Петр', surname: 'Петров', donate: 200},
    {id: 2, name: 'Александр', surname: 'Александров', donate: 400},
  ]
};

const PersonsList = () => {
  const totalDonate = data.persons[0].donate + data.persons[1].donate + data.persons[2].donate;

  return (
    <div>
      <h1>{data.header}</h1>
      <table>
        <thead>
          <tr>
            <th>Имя</th>
            <th>Фамилия</th>
            <th>Сумма взноса</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{data.persons[0].name}</td>
            <td>{data.persons[0].surname}</td>
            <td>{data.persons[0].donate}</td>
          </tr>
          <tr>
            <td>{data.persons[1].name}</td>
            <td>{data.persons[1].surname}</td>
            <td>{data.persons[1].donate}</td>
          </tr>
          <tr>
            <td>{data.persons[2].name}</td>
            <td>{data.persons[2].surname}</td>
            <td>{data.persons[2].donate}</td>
          </tr>
        </tbody>
      </table>
      <p>Общая сумма взносов: {totalDonate}</p>
    </div>
  );
};

export default PersonsList;
