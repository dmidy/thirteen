import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;

public class JsonPlaceholderApiClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static String createUser(String json) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(BASE_URL + "/users");
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
            return responseString;
        }
    }

    public static String updateUser(String json, int userId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(BASE_URL + "/users/" + userId);
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            httpPut.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPut);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
            return responseString;
        }
    }

    public static int deleteUser(int userId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(BASE_URL + "/users/" + userId);
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            return response.getStatusLine().getStatusCode();
        }
    }

    public static String getAllUsers() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(BASE_URL + "/users");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return responseString;
        }
    }

    public static String getUserById(int userId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(BASE_URL + "/users/" + userId);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return responseString;
        }
    }

    public static String getUserByUsername(String username) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(BASE_URL + "/users?username=" + username);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return responseString;
        }
    }

    public static void getCommentsForLastPostAndSaveToFile(int userId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Получаем информацию о последнем посте пользователя
            HttpGet userPostsRequest = new HttpGet(BASE_URL + "/users/" + userId + "/posts");
            CloseableHttpResponse userPostsResponse = httpClient.execute(userPostsRequest);
            HttpEntity userPostsEntity = userPostsResponse.getEntity();
            String userPostsResponseString = EntityUtils.toString(userPostsEntity);

            // Парсим JSON для получения ID последнего поста
            JSONArray userPostsArray = new JSONArray(userPostsResponseString);
            if (userPostsArray.length() == 0) {
                System.out.println("У пользователя нет постов.");
                return;
            }
            int lastPostId = userPostsArray.getJSONObject(userPostsArray.length() - 1).getInt("id");

            // Получаем комментарии к последнему посту
            HttpGet commentsRequest = new HttpGet(BASE_URL + "/posts/" + lastPostId + "/comments");
            CloseableHttpResponse commentsResponse = httpClient.execute(commentsRequest);
            HttpEntity commentsEntity = commentsResponse.getEntity();
            String commentsResponseString = EntityUtils.toString(commentsEntity);

            // Записываем комментарии в файл
            String fileName = "user-" + userId + "-post-" + lastPostId + "-comments.json";
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(commentsResponseString);
                System.out.println("Комментарии сохранены в файл: " + fileName);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getOpenTodosForUser(int userId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet todosRequest = new HttpGet(BASE_URL + "/users/" + userId + "/todos");
            CloseableHttpResponse todosResponse = httpClient.execute(todosRequest);
            HttpEntity todosEntity = todosResponse.getEntity();
            String todosResponseString = EntityUtils.toString(todosEntity);
            return todosResponseString;
            }
        }

    public static void main(String[] args) throws IOException {
        try {
            // Примеры использования методов
            // Создание нового пользователя
            String newUserJson = "{ \"name\": \"John Doe\", \"username\": \"johndoe\", \"email\": \"johndoe@example.com\" }";
            String createdUser = createUser(newUserJson);
            System.out.println("Созданный пользователь: " + createdUser);

            // Обновление информации о пользователе
            int userIdToUpdate = 1; 
            String updatedUserInfo = "{ \"name\": \"Updated Name\", \"username\": \"updatedusername\", \"email\": \"updatedemail@example.com\" }";
            String updatedUser = updateUser(updatedUserInfo, userIdToUpdate);
            System.out.println("Обновленный пользователь: " + updatedUser);

            // Удаление пользователя
            int userIdToDelete = 1; 
            int deleteStatus = deleteUser(userIdToDelete);
            System.out.println("Статус удаления: " + deleteStatus);

            // Получение всех пользователей
            String allUsers = getAllUsers();
            System.out.println("Все пользователи: " + allUsers);

            // Получение информации о пользователе по ID
            int userIdToRetrieve = 1; 
            String userById = getUserById(userIdToRetrieve);
            System.out.println("Пользователь по ID: " + userById);

            // Получение информации о пользователе по имени пользователя (username)
            String usernameToRetrieve = "Samantha"; 
            String userByUsername = getUserByUsername(usernameToRetrieve);
            System.out.println("Пользователь по username: " + userByUsername);

            // Получение и сохранение комментариев к последнему посту пользователя
            int userIdForComments = 1; 
            getCommentsForLastPostAndSaveToFile(userIdForComments);

            // Получение открытых задач для пользователя
            int userIdForTodos = 1; 
            String openTodos = getOpenTodosForUser(userIdForTodos);
            System.out.println("Открытые задачи для пользователя: " + openTodos);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
