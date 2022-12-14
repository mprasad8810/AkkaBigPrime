import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import static akka.actor.typed.javadsl.Behaviors.setup;

public class WorkerBehavior extends AbstractBehavior<WorkerBehavior.Command> {

    public static class Command implements Serializable{
        private static final long serialVersionUID =1L;
        private String message;
        private ActorRef<ManagerBehavior.Command> sender;

        public Command(String message, ActorRef<ManagerBehavior.Command> sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public ActorRef<ManagerBehavior.Command> getSender() {
            return sender;
        }
    }

    private WorkerBehavior(ActorContext<Command> context){
        super(context);
    }

    public static Behavior<Command> create(){
         return Behaviors.setup(WorkerBehavior::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage(command ->{
                    if(command.getMessage().equals("start")){
                        BigInteger bigInteger = new BigInteger(100,new Random());
                        command.getSender().tell(new ManagerBehavior.ResultCommand(bigInteger.nextProbablePrime()));
//                        System.out.println(bigInteger.nextProbablePrime());
                    }
                    return this;
                })
//                .onMessageEquals("start",()->{
//                    BigInteger bigInteger = new BigInteger(100,new Random());
//                    System.out.println(bigInteger.nextProbablePrime());
//                    return this;
//                })
                .build();
    }
}
