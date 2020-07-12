(defproject supermarket-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [cheshire "5.8.1"]
                 [com.stuartsierra/component "0.4.0"]
                 [dawcs/flow "1.0.0"]
                 [clj-http "3.10.1"]
                 [io.pedestal/pedestal.jetty "0.5.5"]
                 [io.pedestal/pedestal.service "0.5.5"]
                 [commons-codec "1.10"]
                 [datomic-schema "1.3.0"]
                 [com.google.guava/guava "21.0"]
                 [environ "1.1.0"]
                 [com.datomic/datomic-pro "0.9.5561" :exclusions [commons-codec
                                                                  joda-time
                                                                  com.google.guava/guava]]
                 [io.pedestal/pedestal.service-tools "0.5.5"]
                 [prismatic/schema "1.1.10"]]
  :resource-paths ["configs"]
  :main ^:skip-aot supermarket-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev  {:repl-options         {:timeout 320000}
                    :env          {:db-connection-uri "datomic:mem://supermarket-dev"
                                   :http-server-port 3000}
                    :source-paths ["src/supermarket_api/dev"]}
             :test  {:env    {:db-connection-uri "datomic:mem://supermarket-test"
                             :http-server-port 3333}}}
  :uberjar-name "api.jar"
  :test-selectors {:default (complement :integration)
                   :integration :integration}
  :repl-options {:init-ns superplace-api.core}
  :repositories [["my.datomic.com" {:url      "https://my.datomic.com/repo"
                                    :username [:env/my_datomic_username]
                                    :password [:env/my_datomic_password]}]])
