package tasks

import play.api.inject.{SimpleModule, _}

class TasksModule extends SimpleModule(bind[TweetsTask].toSelf.eagerly())