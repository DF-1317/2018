/*
 * This test program is intended to demonstrate a hopefully easier facility to modify/change step-by-step tasks
 *
 * This code was developed and tested in a Linux environment.
 * To compile and run:
 * $ javac TaskRunner.java
 * $ java -cp . TaskRunner
 * the output produced is:
 *  Starting the Task Runner...
 *  0: tick... [2] A Test Runner
 *  1: ...tock (0) cool stuff1
 *  1: ...tock (1) cool stuff1
 *  1: ...tock (2) cool stuff1
 *  1: ...tock (3) cool stuff1
 *  1: ...tock (4) cool stuff1
 *  1: ...tock (5) cool stuff1
 *  1: ...tock (6) cool stuff1
 *  2: tick... [22] A Test Runner
 *  3: ...tock (7) cool stuff3
 *  Task Runner Complete!
 */

import java.lang.reflect.Method;
import java.util.*;

/*
 * We are using this class to demonstrate the idea of an easily changable task list. First some definitions:
 *
 * task         - an individual step in a longer list of things to do. Each task does its work and then returns
 *                to the dispatching method, run() in our example, before the next task in the list is executed.
 * task list    - an ordered collection of individual tasks. Without some special directives, each task in turn is
 *                executed after its predecessor. The ordering of the tasks in the list is the default ordering that
 *                they will be executed.
 * dispatch method - this method controls the execution of the tasks in the task list. It will manage the parameters
 *                used to call the task method and also control the updating of the program counter. As the tasks
 *                are invoked, the context is modified with the current value of the pc...so a tasks can test to see
 *                what position it has in the list for that invokation. It is valid to have a given task method in
 *                the task list more than once; each may have identical parameters or they can be completely
 *                different.
 * pc           - a shorthand for "program counter". This is a pointer into the task list as to what task is being
 *                executed. Without special instruction, it is incremented by 1 with each iteration of the
 *                dispatching method.
 * context      - this is a collection of global information. It is initialized prior to calling the first task and
 *                can be modified by each of the tasks as it sees fit. It is customery to limit those modifications
 *                to data 'owned' by the task, however any and all of what is in the context is available. It is
 *                implemented as a Map, so values kept in the context can be labeled with nice names [keys].
 * arg list     - this can be thought of as a context for one call to the task. It too is implemented as a Map, thus
 *                allowing for more maintainable code.
 * jump point   - it is possible to direct the dispatch method to not execute the next task in the list, but rather
 *                it can execute next any task in the list. It does this by changing its return value. Task methods are
 *                expected to return an int. If this is a -1, the dispatching method will do its normal thing and
 *                increase the pc by 1. Any other value is treated as the new value for the pc, which means it is
 *                going to jump to another location in the task list. A check is made to ensure this jump point is
 *                a valid location in the task list. If not, then the regular processing of adding 1 to the pc is done.
 */
class TaskRunner {

     Map<String,Object>     context         = new HashMap<String,Object>(); /* global context map */
     List<List>             tasks           = new LinkedList<List>();       /* task list */

     int                    jump1;          /* jump point #1, we can use more meaningful names and should */
     int                    jump2;          /* jump point #2 */
     int                    theEnd;         /* special jump point initialized to the end of the task list */

     /*
      * The program execution starts here. This is a typical kind of main() method...construct an instance
      * of the class we are in and then invoke a method on that instance.
      * For our demonstration, the run() method is called; this is our dispatch method.
      */
     public static void main(String[] args) {
         System.out.println("Starting the Task Runner...");
         TaskRunner tr = new TaskRunner();
         tr.run();
         System.out.println("Task Runner Complete!");
     }

     /*
      * Along with the initialization of the class attributes defined above, we also initialize the
      * global context and task list.
      */
     public TaskRunner() {
         initContext();
         try {
             initTasks();
         } catch (NoSuchMethodException e) { /* If you make a mistake defining the task list, we will hear of it */
             System.err.println("Troubles " + e.getMessage());
             e.printStackTrace();
         }
     }

     /*
      * The dispatch method has a well defined responsibility; call each task in the list in order (usually).
      * After we have completed the task list, we simply returned from whence we were called from.
      * For our demonstration, to say we have completed the task list means that the pc is pointing past
      * the end of the task list.
      */
     public void run() {
         int        pc      = 0;                /* initialize the pc to the first task in the list */
         int        eot     = tasks.size();     /* End Of Task list */

         while (pc < eot) {                         /* keep processing while we have a pc in the list */
             List       entry   = tasks.get(pc);    /* each task in the list is actually a pair */
             Method     m       = (Method) entry.get(0); /* the method to call is first in the entry */
             int        nextPc;                     /* captures where we go next when a task finishes */

             context.put("pc", pc);                 /* update the pc kept in the context */
             nextPc = -1;                           /* and initialize where we go next */

             /*
              * Invoking a method in Java involves 2 or 3 things; the 3rd is optional.
              * First a method is needed. A method is just the definition of some code to run.
              * Second an object instance is needed; that's the data associated with the class object. In our
              * situation we are running in this class, a TaskRunner, and so we just use the 'this' pointer for
              * the instance we are running in.
              * Third would be the parameters to pass to the funtion. In our case there are 2: the context and the
              * argument list. It is possible to have zero parameters.
              */
             try {
                 nextPc = (int) m.invoke(this, context, entry.get(1));
             } catch (Exception e) {                        /* If we have trouble, tell the user where */
                 System.err.println("Trouble at pc: " + pc  /* this will say what task had trouble */
                         + " [" + m.getName() + "] "
                         + e.getMessage());
                 e.printStackTrace();                       /* and this will say what line in that task */
             }

             /* if the nextPC returned by the task is bad, we just move ahead by 1 */
             if (0 > nextPc || tasks.size() < nextPc) pc += 1;
             else pc = nextPc;
         }
     }

     /*
      * This is a helper method to retrieve values from a map. The idiom used does a check for a key in the map
      * before attempting to actually retrieve the value for that key. If available, all is well and the Object is
      * returned. If it is missing, then a default value can be returned instead, one provided by the code calling
      * this method.
      *
      * Using this makes the Java code a bit more compact. The checks and behaviors are no longer replicated in each
      * place the map is accessed. It all shrinks to one line to call this method.
      *
      * One shortcoming of this idiom; the caller cannot tell if the key was or was not in the map. It is possible the
      * default value is what the map happens to have at the time. If knowing this is important to the caller, then
      * they can do the contains test themselves. For the most part however, this does support the other code well.
      */
     private Object getValue(Map m, String key, Object defaultValue) {
         if (m.containsKey(key)) return m.get(key);
         else return defaultValue;
     }

     /*
      * This is the setup for our demonstration context. It is a single parameter with a string value.
      */
     public void initContext() {
         context.put("prog", "A Test Runner");
     }

     /*
      * This and the run() method are the keys to what is being demonstrated with this program.
      *
      * Here is where we load the task list. Because this is Java, the syntax required is a bit drawn out,
      * but once you get it once, a quick copy/paste/edit takes care of the rest.
      * In our demonstration, a total of 5 tasks are put into the list; 2 invocations of 2 methods: tick() and tock()
      * and a somewhat special no_op() method at the very end.
      * More details are located within...
      *
      * After looking at this setup you might ask...Why go to all this trouble? Well the value of this scheme comes
      * to bear as you find yourself changing the other that things need to run in the task list. Just re-arrange
      * the order in the list and things will run in that new order. The dispatch method, run(), instead of looping,
      * can return to the main robot code; it can be the periodic autonomous method called from the WPIlib.
      *
      * It also is possible to have more than one task list active at the same time. They could each share a single
      * context, or have one per-list. That is up to the designer of the dispath method.
      */
     public void initTasks() throws NoSuchMethodException {

         /*
          * Each entry in the task list is actually a list itself; a list of 2 items, or a pair.
          * So, what we are adding to the task list is a new instance of an ArrayList.
          * In the code below, we make use of a special initializer: {{ ... }}
          * In there we are adding the pair of items:
          *   - at index 0 we are adding the Method instance for the tick() method
          *   - at index 1 we are adding a new Map, the one that will contain the arguments for the invocation
          *     + the "stuff" parameter will have a value of "12"...a String 12
          *     + the "prog" parameter will have a value of "cool stuff0"
          *
          * When this is interpreted by the run() method, it in effect will be doing:
          *   rc = tick( ctx, [ stuff : "12", prog : "cool stuff0" ] )
          *   call the tick method with a context and the argument map, putting the return code in rc.
          */
         tasks.add( new ArrayList<Object>(){{
             add(0, TaskRunner.class.getMethod("tick", Map.class, Map.class));
             add(1, new HashMap<String,String>(){{
                 put("stuff", "12");
                 put("prog", "cool stuff0");
             }});
         }});

         /*
          * The remainder of these task list additions do similar things; with the next being an
          * exception.
          *
          * In this case we are recording how full the task list is at this point. We are keeping that in the
          * jump1 variable [this could be kept in the context btw]. This can be used by the worker methods to
          * return to this task if the conditions are right. See the tock() method for one such case.
          */
         jump1 = tasks.size();
         tasks.add( new ArrayList<Object>(){{
             add(0, TaskRunner.class.getMethod("tock", Map.class, Map.class));
             add(1, new HashMap<String,String>(){{
                 put("stuff", "22");
                 put("prog", "cool stuff1");
             }});
         }});

         tasks.add( new ArrayList<Object>(){{
             add(0, TaskRunner.class.getMethod("tick", Map.class, Map.class));
             add(1, new HashMap<String,String>(){{
                 put("stuff", "32");
                 put("prog", "cool stuff2");
             }});
         }});

         tasks.add( new ArrayList<Object>(){{
             add(0, TaskRunner.class.getMethod("tock", Map.class, Map.class));
             add(1, new HashMap<String,String>(){{
                 put("stuff", "42");
                 put("prog", "cool stuff3");
             }});
         }});

         /*
          * Add a terminating, end of list, jump to point.
          * The task at this point in the list does nothing...a "no operation".
          */
         theEnd = tasks.size();
         tasks.add( new ArrayList<Object>(){{
             add(0, TaskRunner.class.getMethod("no_op", Map.class, Map.class));
             add(1, new HashMap<String,String>(){{
             }});
         }});
     }

     /*
      * Here are two methods we have implemented to 'do something' in this application.
      * Their definition follows a common pattern. Other than the name being unique,
      * each:
      *  - is public
      *  - returns an int
      *  - takes 2 parameters
      *    + a map with global context information
      *    + a map with arguments specific to each call
      * The run() method knows about all this.
      */
     public int tick(Map<String,Object> ctx, Map<String, String> args) {
         /*
          * We use the getValue() method to return the value for the stuff parameter,
          * or use a default value of "0" if it is not in there.
          */
         String a = getValue(args, "stuff", "0").toString();
         int val = Integer.parseInt(a) - 10;
         /*
          * The work we do here is just print out some values
          */
         System.out.println(ctx.get("pc").toString() + ": tick..."
                 + " [" + val + "] "
                 + getValue(ctx, "prog", "N/A").toString());

         /*
          * When we return, we want execution to continue with the next task...no jumping around
          */
         return -1;
     }

     /*
      * Just like tick(), we follow the same method signature.
      * We do a slightly different task here, and we demonstrate keeping our own state information
      * in the global context, and also shifting which of the tasks is to be executed next.
      */
     public int tock(Map<String,Object> ctx, Map<String, String> args) {
         String count = getValue(ctx, "tockCounter", "-1").toString();
         int c = Integer.parseInt(count);
         c += 1;
         ctx.put("tockCounter", c);
         System.out.println(ctx.get("pc").toString()
                 + ": ...tock"
                 + " (" + c + ") "
                 + args.get("prog"));

         /*
          * In our case, if we have been called less than 6 times, we want the next task done to
          * be the one at the jump1 point in the task list. Otherwise we are happy with just continuing
          * with the next task after us in the definition.
          * NOTE: it could be the jump1 location is after our task in the list; it could be anywhere,
          *       including pointing at our own task.
          */
         return (c < 6) ? jump1 : -1;
     }

     /*
      * It is handy to have a task that does nothing...in case you wanted to disable a task in the list
      * without changing the size of the list. Well...maybe...8-)
      */
    public int no_op(Map<String,Object> ctx, Map<String, String> args) {
         return -1;
    }
 }