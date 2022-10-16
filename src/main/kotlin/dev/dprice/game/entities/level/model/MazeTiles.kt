package dev.dprice.game.entities.level.model
//
//data class SpriteIndex(val x: Int, val y: Int)
//
//sealed class MazeTile {
//    data class TopLeftCorner(
//        override val spriteIndex: SpriteIndex = SpriteIndex(0, 0)
//    ): MazeTile(), MazeWallTile
//
//    data class TopRightCorner(
//        override val spriteIndex: SpriteIndex = SpriteIndex(0, 0)
//    ): MazeTile(), MazeWallTile
//
//    data class BottomLeftCorner(
//        override val spriteIndex: SpriteIndex = SpriteIndex(0, 0)
//    ): MazeTile(), MazeWallTile
//
//    data class TopLeftCorner(
//        override val spriteIndex: SpriteIndex = SpriteIndex(0, 0)
//    ): MazeTile(), MazeWallTile
//}
//
//sealed interface MazeWallTile {
//    val spriteIndex: SpriteIndex
//}