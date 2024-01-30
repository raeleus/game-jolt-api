package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.raeleus.gamejoltapi.GameJoltDataStore.*;
import com.github.raeleus.gamejoltapi.GameJoltFriends.FriendsFetchListener;
import com.github.raeleus.gamejoltapi.GameJoltFriends.FriendsFetchRequest;
import com.github.raeleus.gamejoltapi.GameJoltFriends.FriendsFetchValue;
import com.github.raeleus.gamejoltapi.GameJoltScores.*;
import com.github.raeleus.gamejoltapi.GameJoltSessions.*;
import com.github.raeleus.gamejoltapi.GameJoltTime.TimeFetchListener;
import com.github.raeleus.gamejoltapi.GameJoltTime.TimeFetchRequest;
import com.github.raeleus.gamejoltapi.GameJoltTime.TimeFetchValue;
import com.github.raeleus.gamejoltapi.GameJoltTrophies.*;
import com.github.raeleus.gamejoltapi.GameJoltUsers.GameJoltUser;
import com.github.raeleus.gamejoltapi.GameJoltUsers.UsersFetchListener;
import com.github.raeleus.gamejoltapi.GameJoltUsers.UsersFetchRequest;
import com.github.raeleus.gamejoltapi.GameJoltUsers.UsersFetchValue;

import java.util.ArrayList;
import java.util.Arrays;

public class Core extends ApplicationAdapter {
    public Stage stage;
    public Skin skin;
    public SystemCursorListener handListener;
    public SystemCursorListener ibeamListener;
    public Label logLabel;
    public TextTooltip logTooltip;
    public Action pingAction;
    public Table friendsTable;
    
    public GameJoltApi gj = new GameJoltApi();
    public final String gameID = "869827";
    public final String key = "78ac632c55945de5cb5f30b735246a8c";
    
    public String username;
    public String token;
    public String serverTime;
    public TextureRegion avatarRegion;
    public Array<GameJoltScore> highScores;
    public int scoreRank;
    public Array<Integer> friends;
    
    public long score;
    public long highestScore;
    public long globalCheese;
    public long personalCheese;
    public long clickTimestamp;
    public long beginTimestamp;
    
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("game-jolt-api");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        new Lwjgl3Application(new Core(), configuration);
    }
    
    public Core() {
    
    }
    
    public Core(String username, String token) {
        this.username = username;
        this.token = token;
    }
    
    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skin.json"));
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        handListener = new SystemCursorListener(SystemCursor.Hand);
        ibeamListener = new SystemCursorListener(SystemCursor.Ibeam);
        
        loginUser();
        
        refreshUI();
    }
    
    public void loginUser() {
        var usernamePair = gj.autoRetrieveUsernameAndToken();
        if (usernamePair != null) {
            username = usernamePair.username;
            token = usernamePair.token;
        }
        
        var requests = new Array<GameJoltRequest>();
        
        var timeFetchRequest = TimeFetchRequest.builder()
                .gameID(gameID)
                .build();
        requests.add(timeFetchRequest);
        
        if (username != null && token != null) {
            var sessionsOpenRequest = SessionsOpenRequest.builder()
                    .gameID(gameID)
                    .username(username)
                    .userToken(token)
                    .build();
            requests.add(sessionsOpenRequest);
            var usersFetchRequest = UsersFetchRequest.builder()
                    .gameID(gameID)
                    .username(username)
                    .build();
            requests.add(usersFetchRequest);
            
            var scoresFetchRequest = ScoresFetchRequest.builder()
                    .gameID(gameID)
                    .tableID(883883)
                    .username(username)
                    .userToken(token)
                    .build();
            requests.add(scoresFetchRequest);
            
            var getKeysRequest = DataStoreGetKeysRequest.builder()
                    .gameID(gameID)
                    .username(username)
                    .userToken(token)
                    .build();
            requests.add(getKeysRequest);
            
            var removeTrophyRequest = TrophiesRemoveAchievedRequest.builder()
                    .gameID(gameID)
                    .trophyID(222003)
                    .username(username)
                    .userToken(token)
                    .build();
            requests.add(removeTrophyRequest);
            
            var friendsFetchRequest = FriendsFetchRequest.builder()
                    .gameID(gameID)
                    .username(username)
                    .userToken(token)
                    .build();
            requests.add(friendsFetchRequest);
        }
        
        gj.sendBatchRequest(requests, gameID, key, false, true,
                new SessionsOpenListener() {
                    @Override
                    public void sessionsOpen(SessionsOpenValue value) {
                        if (!value.success) updateLogLabel("Batch request unsuccessful: " + value.message);
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("Batch request connection cancelled");
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("Batch request failed: " + t.toString());
                    }
                },
                new TimeFetchListener() {
                    @Override
                    public void timeFetch(TimeFetchValue value) {
                        if (!value.success) updateLogLabel("Batch request unsuccessful: " + value.message);
                        else {
                            serverTime = value.toString();
                            refreshUI();
                        }
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("Batch request connection cancelled");
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("Batch request failed: " + t.toString());
                    }
                },
                new UsersFetchListener() {
                    @Override
                    public void usersFetch(UsersFetchValue value) {
                        if (!value.success || value.users.size == 0) {
                            logoutUser();
                            updateLogLabel("Batch request unsuccessful: " + value.message);
                            return;
                        }
                        gj.downloadImageUrlAsTextureRegion(value.users.first().avatarURL, region -> {
                            avatarRegion = region;
                            refreshUI();
                            updateLogLabel("Logged in!");
                            pingAction = Actions.forever(Actions.delay(30f, Actions.run(() -> pingSession())));
                            stage.addAction(pingAction);
                        });
                        refreshUI();
                    }
                    
                    @Override
                    public void cancelled() {
                        logoutUser();
                        refreshUI();
                        updateLogLabel("Batch request connection cancelled");
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        logoutUser();
                        refreshUI();
                        updateLogLabel("Batch request failed: " + t.toString());
                    }
                },
                new ScoresFetchListener() {
                    @Override
                    public void scoresFetch(ScoresFetchValue value) {
                        if (!value.success) updateLogLabel("Batch request unsuccessful: " + value.message);
                        else {
                            highestScore = value.getScores().first().sort;
                        }
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("Batch request connection cancelled");
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("Batch request failed: " + t.toString());
                    }
                }, new DataStoreGetKeysListener() {
                    @Override
                    public void dataStoreGetKeys(DataStoreGetKeysValue value) {
                        if (!value.success) updateLogLabel("Batch request unsuccessful: " + value.message);
                        else {
                            if (!value.keys.contains("cheese-count", false)) setPersonalCheeseInitialValue();
                        }
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("Batch request connection cancelled");
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("Batch request failed: " + t.toString());
                    }
                }, new TrophiesRemoveAchievedListener() {
                    @Override
                    public void trophiesRemoveAchieved(TrophiesRemoveAchievedValue value) {
                        if (!value.success) updateLogLabel("TrophiesRemove unsuccessful: " + value.message);
                        else {
                            updateLogLabel("Trophy removed!");
                        }
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("TrophiesRemove connection cancelled");
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("TrophiesRemove failed: " + t.toString());
                    }
                }, new FriendsFetchListener() {
                    @Override
                    public void friendsFetch(FriendsFetchValue value) {
                        if (!value.success) updateLogLabel("Batch request unsuccessful: " + value.message);
                        else {
                            friends = value.friends;
                            refreshUI();
                            refreshFriendsTable();
                        }
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("Batch request connection cancelled");
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("Batch request failed: " + t.toString());
                    }
                });
    }
    
    public void logoutUser() {
        stage.getRoot().removeAction(pingAction);
        closeSession();
        
        username = null;
        token = null;
        highestScore = 0;
        scoreRank = 0;
        
        if (avatarRegion != null) avatarRegion.getTexture().dispose();
        avatarRegion = null;
        
        refreshUI();
        updateLogLabel("Logged out!");
    }
    
    public void refreshUI() {
        stage.clear();
        
        var root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("bg-10"));
        stage.addActor(root);
        
        if (serverTime == null) {
            var label = new Label("LOADING...", skin, "button");
            root.add(label);
            return;
        }
        
        var header = new Table();
        header.setBackground(skin.getDrawable("header-10"));
        root.add(header).growX();
        
        var table = new Table();
        header.add(table);
        
        var label = new Label("Game Jolt API", skin, "title");
        table.add(label);
        
        table.row();
        label = new Label("by Raeleus", skin, "button");
        table.add(label).left();
        
        table = new Table();
        header.add(table).expandX().right();
        
        if (username != null) {
            table.defaults().left().space(5);
            label = new Label(username, skin, "button");
            table.add(label);
            
            table.row();
            label = new Label("Logged in", skin);
            table.add(label);
            
            table.row();
            var textButton = new TextButton("Log Out", skin, "link");
            table.add(textButton);
            addHandListener(textButton);
            onChange(textButton, this::logoutUser);
        } else {
            table.defaults().left().space(5);
            label = new Label("Welcome Guest!", skin, "button");
            table.add(label);
            
            table.row();
            var textButton = new TextButton("Log in", skin, "link");
            table.add(textButton);
            addHandListener(textButton);
            onChange(textButton, this::showLogin);
        }
        
        table = new Table();
        table.setBackground(skin.getDrawable("frame-10"));
        header.add(table).padLeft(10);
        
        if (avatarRegion == null) {
            var image = new Image(skin, "unknown-user");
            image.setScaling(Scaling.fit);
            table.add(image).size(64);
        } else {
            var image = new Image(avatarRegion);
            image.setScaling(Scaling.fit);
            table.add(image).size(64);
        }
        
        root.row();
        var body = new Table();
        body.top().pad(20);
        root.add(body).grow();
        
        table = new Table();
        body.add(table);
        
        table.defaults().space(10);
        var textButton = new TextButton("Play \"Game\"", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, this::showGame);
        
        table.row();
        textButton = new TextButton("Highscores", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, this::showHighScores);
        
        table.row();
        textButton = new TextButton("Statistics", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, this::showStatistics);
        
        table = new Table();
        body.add(table).expandX().right();
        
        label = new Label("Friends", skin, "time");
        table.add(label).left().space(10);
        
        table.row();
        friendsTable = new Table();
        friendsTable.setBackground(skin.getDrawable("section-10"));
        friendsTable.top();
        table.add(friendsTable).minWidth(200);
        
        friendsTable.defaults().space(10);
        
        root.row();
        table = new Table();
        root.add(table).growX();
        
        logLabel = new Label("", skin, "error");
        logLabel.setEllipsis("...");
        logLabel.setTouchable(Touchable.enabled);
        table.add(logLabel).padLeft(10).growX().minWidth(0);
        TooltipManager.getInstance().initialTime = 0;
        TooltipManager.getInstance().hideAll();
        logTooltip = new TextTooltip("", skin);
        logLabel.addListener(logTooltip);
        
        var button = new Button(new ButtonStyle());
        table.add(button).expandX().right().pad(10);
        addHandListener(button);
        
        label = new Label("Last server time:", skin);
        button.add(label).space(10);
        
        label = new Label(serverTime, skin, "time");
        button.add(label);
    }
    
    public void refreshFriendsTable() {
        if (friendsTable == null) return;
        
        var arrayList = new ArrayList<Integer>();
        for (var friend : friends) {
            arrayList.add(friend);
        }
        
        var request = UsersFetchRequest.builder()
                .gameID(gameID)
                .userIDs(arrayList)
                .build();
        
        gj.sendRequest(request, key, new UsersFetchListener() {
            @Override
            public void usersFetch(UsersFetchValue value) {
                if (!value.success) updateLogLabel("Friends unsuccessful: " + value.message);
                else {
                    for (GameJoltUser user : value.users) {
                        var label = new Label(user.username, skin);
                        friendsTable.add(label);
                        friendsTable.row();
                    }
                }
            }

            @Override
            public void cancelled() {
                updateLogLabel("Friends connection cancelled");
            }

            @Override
            public void failed(Throwable t) {
                updateLogLabel("Friends failed: " + t.toString());
            }
        });
    }
    
    public void showGame() {
        score = 0;
        var dialog = new Dialog("", skin);
        var table = dialog.getContentTable();
        table.pad(20).padBottom(10);
        
        var label = new Label("Click the cookie!", skin, "title");
        table.add(label);
        
        table.row();
        var image = new Image(skin, "cookie");
        image.setTouchable(Touchable.enabled);
        table.add(image);
        
        table.row();
        var scoreLabel = new Label("0", skin, "title");
        table.add(scoreLabel);
        onClick(image, () -> {
            var timestamp = TimeUtils.millis();
            if (score == 0) beginTimestamp = timestamp;
            clickTimestamp = timestamp;
            scoreLabel.setText(Long.toString(++score));
        });
        
        table = dialog.getButtonTable();
        table.pad(10);
        var textButton = new TextButton("I'm done", skin);
        table.add(textButton).uniformX().fillX();
        addHandListener(textButton);
        onChange(textButton, () -> {
            if (score > 0) {
                if (score > highestScore) highestScore = score;
                if (username != null) {
                    submitScore();
                    awardTrophy();
                }
                else showGuestScoreSubmission();
            }
            dialog.hide();
        });
        
        dialog.show(stage);
    }
    
    public void submitScore() {
        var clicksPerMillisecond = clickTimestamp - beginTimestamp == 0 ? 0 : (float) score / (clickTimestamp - beginTimestamp);
        
        var request = ScoresAddRequest.builder()
                .gameID(gameID)
                .username(username)
                .userToken(token)
                .tableID(883883)
                .score(score + " cookies")
                .sort(score)
                .extraData(Float.toString(clicksPerMillisecond))
                .build();
        
        gj.sendRequest(request, key, new ScoresAddListener() {
            @Override
            public void scoresAdd(ScoresAddValue value) {
                if (!value.isSuccess()) updateLogLabel("scoresAdd unsuccessful: " + value.message);
                else updateLogLabel("Added user score!");
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("scoresAdd failed: " + t.toString());
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("scoresAdd connection cancelled");
            }
        });
    }
    
    public void updateLogLabel(String log) {
        logLabel.setText(log);
        logTooltip.getActor().setText(log);
    }
    
    public void showGuestScoreSubmission() {
        var dialog = new Dialog("", skin);
        var table = dialog.getContentTable();
        table.pad(20).padBottom(10);
        
        var label = new Label("Guest name:", skin);
        table.add(label);
        
        var usernameField = new TextField("", skin);
        table.add(usernameField);
        addIbeamListener(usernameField);
        
        table = dialog.getButtonTable();
        table.pad(10);
        var loginButton = new TextButton("Log in", skin);
        table.add(loginButton).uniformX().fillX();
        addHandListener(loginButton);
        onChange(loginButton, () -> {
            var guest = usernameField.getText();
            submitGuestScore(guest);
            dialog.hide();
        });
        usernameField.setTextFieldListener((textField, c) -> {
            if (c == '\n') loginButton.setChecked(true);
        });
        
        var textButton = new TextButton("Cancel", skin);
        table.add(textButton).uniformX().fillX();
        addHandListener(textButton);
        onChange(textButton, dialog::hide);
        
        dialog.show(stage);
        stage.setKeyboardFocus(usernameField);
    }
    
    public void submitGuestScore(String guest) {
        var clicksPerMillisecond = clickTimestamp - beginTimestamp == 0 ? 0 : (float) score / (clickTimestamp - beginTimestamp);
        
        var request = ScoresAddRequest.builder()
                .gameID(gameID)
                .guest(guest)
                .tableID(883883)
                .score(score + " cookies")
                .sort(score)
                .extraData(Float.toString(clicksPerMillisecond))
                .build();
        
        gj.sendRequest(request, key, new ScoresAddListener() {
            @Override
            public void scoresAdd(ScoresAddValue value) {
                if (!value.isSuccess()) updateLogLabel("scoresAdd unsuccessful: " + value.message);
                else updateLogLabel("Added guest score!");
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("scoresAdd failed: " + t.toString());
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("scoresAdd connection cancelled");
            }
        });
    }
    
    private void fetchHighScores(Runnable runnable) {
        var requests = new Array<GameJoltRequest>();
        
        if (highestScore > 0) {
            var getRankRequest = ScoresGetRankRequest.builder()
                    .gameID(gameID)
                    .tableID(883883)
                    .sort(highestScore)
                    .build();
            requests.add(getRankRequest);
        }
        
        var scoresFetchRequest = ScoresFetchRequest.builder()
                .gameID(gameID)
                .tableID(883883)
                .limit(20)
                .build();
        requests.add(scoresFetchRequest);
        
        gj.sendBatchRequest(requests, gameID, key, false, false,
                new ScoresGetRankListener() {
                    @Override
                    public void scoresGetRank(ScoresGetRankValue value) {
                        if (!value.isSuccess()) updateLogLabel("scoresGetRank unsuccessful: " + value.message);
                        else {
                            scoreRank = value.getRank();
                        }
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("scoresGetRank failed: " + t.toString());
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("scoresGetRank connection cancelled");
                    }
                },
                new ScoresFetchListener() {
                    @Override
                    public void scoresFetch(ScoresFetchValue value) {
                        if (!value.isSuccess()) updateLogLabel("scoresFetch unsuccessful: " + value.message);
                        else {
                            highScores = value.getScores();
                            updateLogLabel("Download the scores!");
                            runnable.run();
                        }
                    }
                    
                    @Override
                    public void failed(Throwable t) {
                        updateLogLabel("scoresFetch failed: " + t.toString());
                    }
                    
                    @Override
                    public void cancelled() {
                        updateLogLabel("scoresFetch connection cancelled");
                    }
                });
    }
    
    public void showHighScores() {
        var dialog = new Dialog("", skin);
        var table = dialog.getContentTable();
        table.pad(20).padBottom(10);
        
        var label = new Label("High Scores", skin, "title");
        table.add(label);
        
        table.row();
        var scoresTable = new Table();
        scoresTable.pad(10);
        var scrollPane = new ScrollPane(scoresTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        table.add(scrollPane).size(200, 200);
        
        label = new Label("loading", skin);
        scoresTable.add(label);
        
        table.row();
        var rankLabel = new Label("Loading rank...", skin);
        table.add(rankLabel);
        
        table = dialog.getButtonTable();
        table.pad(10);
        var textButton = new TextButton("Close", skin);
        table.add(textButton).uniformX().fillX();
        addHandListener(textButton);
        onChange(textButton, () -> {
            highScores = null;
            dialog.hide();
        });
        
        dialog.show(stage);
        stage.setScrollFocus(scrollPane);
        
        fetchHighScores(() -> {
            scoresTable.clear();
            scoresTable.defaults().left();
            int rank = 0;
            for (GameJoltScore gameJoltScore : highScores) {
                var name = !gameJoltScore.user.equals("") ? gameJoltScore.user : gameJoltScore.guest;
                var scoreLabel = new Label(++rank + ". " + name + ": " + gameJoltScore.score, skin);
                scoresTable.add(scoreLabel);
                scoresTable.row();
            }
            
            
            if (scoreRank > 0) rankLabel.setText("Your rank is #" + scoreRank);
            else rankLabel.setText("");
        });
    }
    
    public void setGlobalCheeseInitialValue() {
        var request = DataStoreSetRequest.builder()
                .gameID(gameID)
                .key("cheese-count")
                .data("0")
                .build();
        
        gj.sendRequest(request, key, new DataStoreSetListener() {
            @Override
            public void dataStoreSet(DataStoreSetValue value) {
                if (!value.isSuccess()) updateLogLabel("DataStoreSet unsuccessful: " + value.message);
                else {
                    updateLogLabel("Set a value in the data store!");
                }
                
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("DataStoreSet failed: " + t.toString());
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("DataStoreSet connection cancelled");
            }
        });
    }
    
    public void setPersonalCheeseInitialValue() {
        var request = DataStoreSetRequest.builder()
                .gameID(gameID)
                .username(username)
                .userToken(token)
                .key("cheese-count")
                .data("0")
                .build();
        
        gj.sendRequest(request, key, new DataStoreSetListener() {
            @Override
            public void dataStoreSet(DataStoreSetValue value) {
                if (!value.isSuccess()) updateLogLabel("DataStoreSet unsuccessful: " + value.message);
                else {
                    updateLogLabel("Set a value in the data store!");
                }
                
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("DataStoreSet failed: " + t.toString());
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("DataStoreSet connection cancelled");
            }
        });
    }
    
    public void updateCheese(Runnable globalUpdateRunnable, Runnable personalUpdateRunnable) {
        var requests = new Array<GameJoltRequest>();
        
        var globalUpdateRequest = DataStoreUpdateRequest.builder()
                .gameID(gameID)
                .key("cheese-count")
                .operation(OperationType.ADD)
                .value("1")
                .build();
        requests.add(globalUpdateRequest);
        
        if (username != null) {
            var personalUpdateRequest = DataStoreUpdateRequest.builder()
                    .gameID(gameID)
                    .username(username)
                    .userToken(token)
                    .key("cheese-count")
                    .operation(OperationType.ADD)
                    .value("1")
                    .build();
            requests.add(personalUpdateRequest);
        }
        gj.sendBatchRequest(requests, gameID, key, true, false, new DataStoreUpdateListener() {
            @Override
            public void dataStoreUpdate(DataStoreUpdateValue value) {
                if (!value.isSuccess()) updateLogLabel("DataStoreUpdate unsuccessful: " + value.message);
                else {
                    if (value.request == globalUpdateRequest) {
                        globalCheese = Long.parseLong(value.getData());
                        globalUpdateRunnable.run();
                    } else {
                        personalCheese = Long.parseLong(value.getData());
                        personalUpdateRunnable.run();
                    }
                    updateLogLabel("Update the data store!");
                }
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("DataStoreUpdate failed: " + t.toString());
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("DataStoreUpdate connection cancelled");
            }
        });
    }
    
    public void showStatistics() {
        var dialog = new Dialog("", skin);
        var table = dialog.getContentTable();
        table.pad(20).padBottom(10);
        
        var label = new Label("Hah hah! You got cheesed!", skin, "title");
        table.add(label);
        
        table.row();
        var image = new Image(skin, "cheese");
        image.setTouchable(Touchable.enabled);
        table.add(image);
        
        table.row();
        var globalCheeseLabel = new Label("loading", skin, "button");
        table.add(globalCheeseLabel);
        
        table.row();
        var personalCheeseLabel = new Label("", skin, "button");
        table.add(personalCheeseLabel);
        
        table = dialog.getButtonTable();
        table.pad(10);
        var textButton = new TextButton("Close the cheese", skin);
        table.add(textButton).uniformX().fillX();
        addHandListener(textButton);
        onChange(textButton, dialog::hide);
        
        dialog.show(stage);
        updateCheese(() -> globalCheeseLabel.setText(globalCheese + " cheesed and counting..."),
                () -> personalCheeseLabel.setText("You have been cheesed " + personalCheese + " times!"));
    }
    
    public void showLogin() {
        var dialog = new Dialog("", skin);
        var table = dialog.getContentTable();
        table.pad(20).padBottom(10);
        
        var label = new Label("Username:", skin);
        table.add(label);
        
        var usernameField = new TextField("", skin);
        table.add(usernameField);
        addIbeamListener(usernameField);
        
        table.row();
        label = new Label("Token:", skin);
        table.add(label);
        
        var tokenField = new TextField("", skin);
        tokenField.setPasswordMode(true);
        tokenField.setPasswordCharacter('*');
        table.add(tokenField);
        addIbeamListener(tokenField);
        
        table = dialog.getButtonTable();
        table.pad(10);
        var loginButton = new TextButton("Log in", skin);
        table.add(loginButton).uniformX().fillX();
        addHandListener(loginButton);
        onChange(loginButton, () -> {
            username = usernameField.getText();
            token = tokenField.getText();
            loginUser();
            dialog.hide();
        });
        usernameField.setTextFieldListener((textField, c) -> {
            if (c == '\n') loginButton.setChecked(true);
        });
        
        tokenField.setTextFieldListener((textField, c) -> {
            if (c == '\n') loginButton.setChecked(true);
        });
        
        var textButton = new TextButton("Cancel", skin);
        table.add(textButton).uniformX().fillX();
        addHandListener(textButton);
        onChange(textButton, dialog::hide);
        
        dialog.show(stage);
        stage.setKeyboardFocus(usernameField);
    }
    
    public void pingSession() {
        var request = SessionsPingRequest.builder()
                .gameID(gameID)
                .username(username)
                .userToken(token)
                .status(SessionStatus.ACTIVE)
                .build();
        
        gj.sendRequest(request, key, new SessionsPingListener() {
            @Override
            public void sessionsPing(SessionsPingValue value) {
                if (!value.success) updateLogLabel("SessionsPing unsuccessful: " + value.message);
                else {
                    updateLogLabel("Session has been pinged!");
                }
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("SessionsPing connection cancelled");
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("SessionsPing failed: " + t.toString());
            }
        });
    }
    
    public void closeSession() {
        var request = SessionsCloseRequest.builder()
                .gameID(gameID)
                .username(username)
                .userToken(token)
                .build();
        
        gj.sendRequest(request, key, new SessionsCloseListener() {
            @Override
            public void sessionsClose(SessionsCloseValue value) {
                if (!value.success) updateLogLabel("SessionsClose unsuccessful: " + value.message);
                else {
                    updateLogLabel("Session has been closed!");
                }
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("SessionsClose connection cancelled");
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("SessionsClose failed: " + t.toString());
            }
        });
    }
    
    public void awardTrophy() {
        var request = TrophiesAddAchievedRequest.builder()
                .gameID(gameID)
                .trophyID(222003)
                .username(username)
                .userToken(token)
                .build();
        
        gj.sendRequest(request, key, new TrophiesAddAchievedListener() {
            @Override
            public void trophiesAddAchieved(TrophiesAddAchievedValue value) {
                if (!value.success) updateLogLabel("TrophiesAdd unsuccessful: " + value.message);
                else {
                    updateLogLabel("Trophy awarded!");
                }
            }
            
            @Override
            public void cancelled() {
                updateLogLabel("TrophiesAdd connection cancelled");
            }
            
            @Override
            public void failed(Throwable t) {
                updateLogLabel("TrophiesAdd failed: " + t.toString());
            }
        });
    }
    
    public ChangeListener onChange(Actor actor, Runnable runnable) {
        var changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        };
        actor.addListener(changeListener);
        return changeListener;
    }
    
    public static void onClick(Actor actor, Runnable runnable) {
        actor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
    }
    
    public void addHandListener(Actor actor) {
        actor.addListener(handListener);
    }
    
    public void addIbeamListener(Actor actor) {
        actor.addListener(ibeamListener);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        
        stage.act();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
