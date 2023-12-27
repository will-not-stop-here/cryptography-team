package rushhour.view;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourSolver;
import rushhour.model.RushHourException;
import rushhour.model.Vehicle;

/**
 * RushHour 游戏的命令行界面（CLI）
 *
 * 作者：Lennard
 */
public class RushHourCLI {

       public static void main(String[] args) throws IOException {
              // 帮助菜单
              String helpMenu = "帮助菜单:\n\thelp - 显示此菜单\n\tsolve - 尝试解决游戏\n\tquit - 退出\n\thint - 显示有效移动\n\treset - 重置游戏\n\t<symbol> <UP|DOWN|LEFT|RIGHT> - 在给定方向上移动一格\n";
              // 从用户获取文件名
              Scanner scanner = new Scanner(System.in);
              System.out.print("输入 Rush Hour 文件名（例如：'data/03_00.csv'):");
              String filename = scanner.nextLine().trim();
              File file = new File(filename);
              // 循环直到传入有效的文件名
              while (!file.exists()) {
                     System.out.println("此文件名无效。\n请输入 Rush Hour 文件名:");
                     filename = scanner.nextLine().trim();
                     file = new File(filename);
              }

              // 从文件生成 RushHour 游戏板
              RushHour gameBoard = new RushHour(filename);

              // 存储 gameBoard 映射 <- 让我验证命令是否与映射中的值匹配
              HashMap<Character, Vehicle> gameMap = gameBoard.getVehiclesOnBoard();

              System.out.println("\n---------- 欢迎来到 Rush Hour ----------\n");
              System.out.println("输入 'help' 获取帮助菜单。");
              boolean keepPlaying = true;
              while (keepPlaying) {
                     // 内部循环，直到游戏结束
                     while (!gameBoard.isGameOver()) {

                            System.out.println(gameBoard);

                            System.out.println("移动次数: " + gameBoard.getMoveCount() + "\n");

                            // 询问用户命令 //
                            System.out.print("> ");
                            String command = scanner.nextLine().trim();
                            // 循环直到命令不为空字符串
                            while (command.isEmpty()) {
                                   System.out.print("> ");
                                   command = scanner.nextLine().trim();
                            }
                            // 检查命令是否为菜单命令
                            String menuCommand = "";
                            if (command.equals("help") || command.equals("quit") || command.equals("hint")
                                    || command.equals("reset") || command.equals("solve")) {
                                   menuCommand = command;
                            }

                            // 菜单命令的 switch 语句
                            boolean shouldContinue = false;
                            if (!menuCommand.isEmpty()) {
                                   switch (menuCommand) {
                                          case "help":
                                                 System.out.println(helpMenu);
                                                 shouldContinue = true;
                                                 break;
                                          case "quit":
                                                 System.out.println("游戏结束");
                                                 System.exit(0);
                                          case "hint":
                                                 // 从可能移动的列表中返回一个随机移动
                                                 Random rand = new Random();
                                                 Collection<Move> possibleMoves = gameBoard.getPossibleMoves();
                                                 Object[] possibleMovesArray = possibleMoves.toArray();
                                                 // 获取随机索引
                                                 int randomIndex = rand.nextInt(possibleMovesArray.length);
                                                 System.out.println("尝试 " + possibleMovesArray[randomIndex]);
                                                 shouldContinue = true;
                                                 break;
                                          case "reset":
                                                 System.out.println("新游戏");
                                                 gameBoard = new RushHour(filename);
                                                 shouldContinue = true;
                                                 break;
                                          case "solve":
                                                 long start = System.currentTimeMillis();
                                                 RushHourSolver winningConfig;
                                                 if (RushHourSolver.solvable(gameBoard)) {
                                                        winningConfig = RushHourSolver.solve(gameBoard);
                                                 } else {
                                                        winningConfig = null;
                                                 }
                                                 long end = System.currentTimeMillis();

                                                 if (winningConfig != null) {
                                                        // 获取 solve 方法中获胜移动的列表
                                                        List<Move> winningMoves = winningConfig.getMovesMade();
                                                        // 遍历每个移动
                                                        for (Move move : winningMoves) {
                                                               // 将每个移动插入到 CLI 中
                                                               try {
                                                                      // 尝试进行玩家的移动
                                                                      System.out.print("\n" + "> " + move + "\n");
                                                                      gameBoard.moveVehicle(move);
                                                                      System.out.println(gameBoard);
                                                                      System.out.println("移动次数: " + gameBoard.getMoveCount() + "\n");
                                                               } catch (RushHourException e) {
                                                                      System.out.println(e.getMessage());
                                                               }
                                                        }
                                                 } else {
                                                        System.out.println("未找到解决方案");
                                                 }
                                                 System.out.println("在毫秒内找到解决方案: " + (end - start));
                                                 shouldContinue = true;
                                                 break;
                                          default:
                                                 System.out.println("无效的命令: " + menuCommand + "，输入 'help' 获取菜单。\n");
                                                 shouldContinue = true;
                                                 break;
                                   }
                            }
                            if (shouldContinue) {
                                   continue;
                            }

                            // 处理格式为 | SYMBOL | DIRECTION | 的命令
                            // 获取用户命令并将其拆分为两个部分 | SYMBOL | DIRECTION |

                            String[] splitCommand = command.split(" ");
                            // 检查无效命令
                            if (splitCommand.length < 2) {
                                   System.out.println("无效的命令。 输入 'help' 获取菜单。\n");
                                   continue;
                            }
                            Character vehicleSymbol = splitCommand[0].toUpperCase().charAt(0);

                            String moveDirection = splitCommand[1].toUpperCase();
                            // 尝试将 moveDirection 转换为 Direction 枚举
                            Direction direction;
                            try {
                                   direction = Direction.valueOf(moveDirection);
                            } catch (IllegalArgumentException e) {
                                   System.out.println(moveDirection + " 不是有效的方向\n");
                                   continue;
                            }

                            // 验证命令组件是否匹配预期值
                            Move playerMove;
                            if (!gameMap.containsKey(vehicleSymbol)) {
                                   System.out.println("无效的移动: 车辆不存在\n");
                                   continue;
                            } else {
                                   // 将命令值分配给 move 对象
                                   playerMove = new Move(vehicleSymbol, direction);
                            }

                            // 尝试进行玩家的移动
                            try {
                                   gameBoard.moveVehicle(playerMove);

                            } catch (RushHourException e) {
                                   System.out.println(e.getMessage());
                            }
                     }
                     // 胜利消息
                     System.out.println("\n做得好！\n");
                     String decision = "";
                     while (decision.isEmpty()) {
                            System.out.println("输入 'reset' 重新开始游戏\n输入 'quit' 结束程序");
                            System.out.print("> ");
                            decision = scanner.nextLine().trim();
                     }
                     if (decision.equals("reset")) {
                            System.out.println("\n新游戏\n");
                            gameBoard = new RushHour(filename);
                            keepPlaying = true;
                     }
                     if (decision.equals("quit")) {
                            keepPlaying = false;
                     }
              }
              System.out.println("再见！");
              scanner.close();
       }
}
