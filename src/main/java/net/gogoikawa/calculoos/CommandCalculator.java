package net.gogoikawa.calculoos;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Stack;

public class CommandCalculator implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        // Parse the calculation from the command arguments
        String calculation = StringArgumentType.getString(context, "calculation");

        // Evaluate the calculation
        double result = evaluate(calculation);

        // Send the result to the player
        source.sendFeedback(Text.of("Result: " + result), false);

        return Command.SINGLE_SUCCESS;
    }

    private double evaluate(String calculation) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < calculation.length(); i++) {
            char c = calculation.charAt(i);

            if (Character.isDigit(c)) {
                // Extract the whole number
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                while (i + 1 < calculation.length() && Character.isDigit(calculation.charAt(i + 1))) {
                    sb.append(calculation.charAt(i + 1));
                    i++;
                }

                // Convert to double and push onto the values stack
                double value = Double.parseDouble(sb.toString());
                values.push(value);
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
                // Push the operator onto the operators stack
                operators.push(c);
            } else if (c == '(') {
                // Push the open parenthesis onto the operators stack
                operators.push(c);
            } else if (c == ')') {
                // Evaluate the expression inside the parenthesis
                while (operators.peek() != '(') {
                    double result = applyOperator(values.pop(), values.pop(), operators.pop());
                    values.push(result);
                }
                operators.pop(); // Discard the open parenthesis
            }
        }

        // Evaluate any remaining operators
        while (!operators.empty()) {
            double result = applyOperator(values.pop(), values.pop(), operators.pop());
            values.push(result);
        }

        // The final value on the stack is the result of the calculation
        return values.pop();
    }

    private double applyOperator(double b, double a, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            case '^':
                return Math.pow(a, b); //super
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
